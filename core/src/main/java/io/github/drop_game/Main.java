package io.github.drop_game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    Texture backgroundTexture;
    Texture bucketTexture;
    Texture dropTexture;
    Sound dropSound;
    Music music;
    FitViewport viewport;
    Sprite bucketSprite;
    Vector2 touchPos;
    Array<Sprite> dropSprites;
    float dropTimer;
    int score;
    boolean gameOver=false;
    BitmapFont font;
    Skin skin;

    Stage stage;

    Rectangle bucketRectangle;
    Rectangle dropRectangle;


    @Override
    public void create() {

        batch = new SpriteBatch();
        viewport = new FitViewport(8,5);
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1,1);
        touchPos = new Vector2();
        dropSprites = new Array<>();
        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();

        font = new BitmapFont();

        score=0;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        music.setLooping(true);
        music.setVolume(.5f);
        music.play();

    }

    @Override
    public void render() {

        if (gameOver) {
            ScreenUtils.clear(Color.RED);
        } else {
            ScreenUtils.clear(Color.BLACK);
        }
        if (!gameOver) {
            input();
            logic();
        }
        else {
            if (Gdx.input.isTouched()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                resetGame();
                input();
                logic();
            }

        }
        draw();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    private void resetGame() {

        ScreenUtils.clear(Color.BLACK);
        score = 0;
        dropSprites.clear();
        gameOver = false;


        music.play();

    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(),0,worldWidth - bucketWidth));

        float delta = Gdx.graphics.getDeltaTime();

        for (int i = dropSprites.size -1 ; i>=0;i--){
            Sprite dropSprite = dropSprites.get(i);

            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f*delta);

            bucketRectangle.set(bucketSprite.getX(),bucketSprite.getY(),bucketWidth,bucketHeight);
            dropRectangle.set(dropSprite.getX(),dropSprite.getY(),dropWidth,dropHeight);

            if (dropSprite.getY()< -dropSprite.getHeight()) {
                gameOver = true;
//                showGameOverDialog();
                return;
            }
            if (bucketRectangle.overlaps(dropRectangle)) {
                dropSound.play();
                dropSprites.removeIndex(i);
                score++;
            }
        }
        dropTimer += delta;
        if (dropTimer>1f){
            dropTimer=0;
            createDropLet();
        }
    }



    private void input() {
        float speed= 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT )){
            bucketSprite.translateX(speed * delta);
        }else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            bucketSprite.translateX(-speed * delta);
        }

        if (Gdx.input.isTouched()){
            touchPos.set(Gdx.input.getX(),Gdx.input.getY());
            viewport.unproject(touchPos);
            bucketSprite.setCenterX(touchPos.x);
        }

    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        font.setColor(Color.WHITE);
        font.getData().setScale(0.08f,0.1f);

        font.draw(batch, "Puntuació: " + score, worldWidth *0.05f, worldHeight);

        bucketSprite.draw(batch);

        for (Sprite dropSprite : dropSprites) {
            dropSprite.draw(batch);
        }

        if (gameOver) {
            batch.setColor(1, 0, 0, 0.5f);
            batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
            batch.setColor(Color.RED);


        }

        batch.end();

    }

    private void createDropLet(){
        float dropWidth=1;
        float dropHeight=1;
        float worldWidth= viewport.getWorldWidth();
        float worldHeight= viewport.getWorldHeight();

        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth,dropHeight);
        dropSprite.setX(MathUtils.random(0f,worldWidth -dropWidth));
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void dispose() {
        batch.dispose();
        bucketTexture.dispose();
        backgroundTexture.dispose();
        dropTexture.dispose();
    }
//    private void showGameOverDialog() {
//        Dialog dialog = new Dialog("Game Over", skin) {
//            @Override
//            protected void result(Object object) {
//                if (object.equals(true)) {
//                    Gdx.app.exit();
//                }
//            }
//        };
//
//        dialog.text("Has perdut!\nPuntuació final: " + score);
//        dialog.button("OK", true);
//        dialog.pack();
//        dialog.show(stage);
//    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
