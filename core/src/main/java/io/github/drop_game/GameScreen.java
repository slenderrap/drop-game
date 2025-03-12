package io.github.drop_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

    private Drop game;
    private SpriteBatch batch;
    private Texture backgroundTexture, bucketTexture, dropTexture;
    private Sound dropSound;
    private Music music;
    private FitViewport viewport;
    private Sprite bucketSprite;
    private Vector2 touchPos;
    private Array<Sprite> dropSprites;
    private float dropTimer;
    private int score;
    private boolean gameOver = false;
    private BitmapFont font;

    Rectangle bucketRectangle;
    Rectangle dropRectangle;



    public GameScreen(Drop game){

        this.game = game;
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

        bucketRectangle= new Rectangle();
        dropRectangle = new Rectangle();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().scale(0.5f);

        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (gameOver){
            game.setScreen(new GameOver(game));
        }
        if (!gameOver){
            handleInput();
            updateLogic(delta);
        }
        draw();

    }

    private void handleInput(){
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

    private void updateLogic(float delta){

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(),0,worldWidth - bucketWidth));

        for (int i = dropSprites.size -1 ; i>=0;i--){
            Sprite dropSprite = dropSprites.get(i);

            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f*delta);
            bucketRectangle.set(bucketSprite.getX(),bucketSprite.getY(),bucketWidth,bucketHeight);
            dropRectangle.set(dropSprite.getX(),dropSprite.getY(),dropWidth,dropHeight);

            if (dropSprite.getY()< -dropSprite.getHeight()) {
                gameOver = true;
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

    private void draw(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        batch.draw(backgroundTexture,0,0,viewport.getWorldWidth(),viewport.getWorldHeight());
        font.draw(batch, "Puntuació: " +score , 10,viewport.getWorldHeight()-10);
        bucketSprite.draw(batch);

        for (Sprite dropSprite: dropSprites){
            dropSprite.draw(batch);
        }

        batch.end();
    }

    public void createDrop(){
        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(1,1);
        dropSprite.setPosition(MathUtils.random(0,viewport.getWorldWidth()-1), viewport.getWorldHeight());
        dropSprites.add(dropSprite);
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true);
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

        backgroundTexture.dispose();
        bucketTexture.  dispose();
        dropTexture.dispose();
        dropSound.dispose();
        music.dispose();
        font.dispose();

    }
}
