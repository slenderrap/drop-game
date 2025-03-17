package io.github.drop_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final Drop game;
    public MainMenuScreen(final Drop game) {
        this.game =game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.font.getData().setScale(0.05f);
        game.font.draw(game.batch, "Welcome to Drop!!!",game.viewport.getWorldWidth()/6,game.viewport.getWorldHeight()/1.5f);
        game.font.getData().setScale(0.02f);
        game.font.draw(game.batch, "Tap anywhere to play!!",(game.viewport.getWorldWidth())/3,1);

        game.batch.end();

        if (Gdx.input.isTouched()){
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width,height,true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
