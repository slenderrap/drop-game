package io.github.drop_game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TestMain extends ApplicationAdapter {
    SpriteBatch batch;
    ScreenViewport viewport;
    Stage stage;
    AssetManager assetManager;
    Skin skin;
    Texture texture;
    BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new ScreenViewport(); // O FitViewport si lo prefieres
        stage = new Stage(viewport);    // Asegúrate de crear el stage

        Gdx.input.setInputProcessor(stage); // Esto asegura que los eventos de entrada se gestionen en el stage

        skin = new Skin();
        font = new BitmapFont();

        skin.add("default-font", font);
        skin.add("white", Color.WHITE);

        Pixmap pixmap = new Pixmap(200, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.8f);  // Color negro con transparencia
        pixmap.fillRectangle(0, 0, 200, 100);
        Texture texture = new Texture(pixmap);
        skin.add("default-rect", new TextureRegionDrawable(new TextureRegion(texture)));
        pixmap.dispose();

        skin.load(Gdx.files.internal("uiskin.json")); // Cargar el skin

        // Crear y mostrar un label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.WHITE;

        Label label = new Label("Hola, Mundo!", labelStyle);
        label.setPosition(100, 200);
        stage.addActor(label);

        // Crear y mostrar un botón
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = skin.getFont("default-font");

        TextButton button = new TextButton("Click Me", buttonStyle);
        button.setPosition(100, 150);
        stage.addActor(button);
    }

    @Override
    public void render() {
        // Limpia la pantalla con color negro
        ScreenUtils.clear(Color.BLACK);

        // Dibuja el fondo de la escena y todos los actores (como Label y TextButton)
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
