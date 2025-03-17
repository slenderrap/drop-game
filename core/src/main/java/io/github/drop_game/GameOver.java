package io.github.drop_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOver implements Screen {

    final Drop game;

    public GameOver(Drop game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(new Color(139/255f, 0, 0,1));

        game.viewport.apply();

        game.batch.begin();

        game.font.getData().setScale(0.05f);
        game.font.draw(game.batch, "GAME OVER",game.viewport.getWorldWidth()/4,game.viewport.getWorldHeight()/1.5f);
        game.font.getData().setScale(0.02f);
        game.font.draw(game.batch, "Tap anywhere to restart!!",(game.viewport.getWorldWidth())/3,1);
        game.font.setColor(Color.YELLOW);


        game.batch.end();
        if (Gdx.input.isTouched()){
            game.setScreen(new GameScreen(game));
            ScreenUtils.clear(Color.BLACK);
        }
    }

    @Override
    public void resize(int width, int height) {

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
