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

        ScreenUtils.clear(Color.RED);

        game.viewport.apply();

        game.batch.begin();

        game.font.draw(game.batch, "GAME OVER",1,1.5f);
        game.font.draw(game.batch, "Tap anywhere to play!!",1,1);
        game.font.setColor(Color.YELLOW);

        game.batch.end();
        if (Gdx.input.isTouched()){
            game.setScreen(new GameScreen(game));
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
