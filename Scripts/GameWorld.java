SpatialObject player;
float playerPosition;
private boolean start, death;
ParticleEmitter deathParticle;
private float particleDisappear;

ObjectFile spawnEnemy;
SpatialObject spawnEnemyPnt;
private float spawnEnemyTime;

public float score, highScore;
UITextView scoreText;

UIRect menuRect;
UIButton settingsBtn;
UIVerticalLayout settingsMenuLayout;
private boolean toggleSettingsMenu;

UIButton settingsReset;
UIRotateImage settingsSongCross;

UIButton startBtn;

UIRect resultsRect;
UITextView resultsText;
UIButton retryBtn;
WorldFile retryWorld;

private float fps;
UITextView fpsText;

void start() {
    highScore = SaveGame.loadFloat("highScore");
    settingsMenuLayout.setSpacing(-140);
}

void repeat() {
    if (startBtn.isDown()) {
        start = true;
    }
    if (start && !death) {
        playable();
        menuRect.getObject().setEnabled(false);
    }
    
    if (death) {
        if (particleDisappear >= 1) {
            deathParticle.setAllowEmission(false);
        } else {
            particleDisappear += Math.bySecond();
        }
        resultsRect.getObject().setEnabled(true);
        resultsText.setText("Your Score: " + (int) score + "\nHighscore: " + (int) highScore);
        
        if (retryBtn.isDown()) {
            WorldController.loadWorld(retryWorld);
        }
    }
    
    settings();
}

void playable() {
    player.getPosition().setY(playerPosition);
    player.lookTo(0, playerPosition, 1);
    playerPosition -= Math.bySecond(Input.getMaxSlide().getY() / 4);
    
    deathParticle.getObject().getPosition().setY(playerPosition);
    score += Math.bySecond();
    scoreText.setText("Score: " + (int) score);
    
    if (playerPosition > 1.5 || playerPosition < -1.5 || player.colliderWithName("Enemy")) {
        SaveGame.saveFloat("highScore", highScore);
        death = true;
        deathParticle.setAllowEmission(true);
        player.setEnabled(false);
    }
    
    if (spawnEnemyTime < 0) {
        spawnEnemyPnt.instantiate(spawnEnemy);
        spawnEnemyTime = Random.range(0.5f, 2f);
    } else {
        spawnEnemyTime -= Math.bySecond();
        spawnEnemyPnt.getPosition().setY(Random.range(-1.5f, 1.5f));
    }
    
    if (score > highScore) {
        highScore = score;
    }
}

void settings() {
    fps = 1 / Math.bySecond();
    fpsText.setText("" + (int) fps);
    
    if (fps < 20) {
        Toast.showText("Oops! Your performance will be low and the game will crash. :(", 0);
        GameController.quit();
    }
    
    if (toggleSettingsMenu) {
        settingsMenuLayout.setSpacing((int) Math.lerp(settingsMenuLayout.getSpacing(), 20, 25));
        if (settingsReset.isDown()) {
            SaveGame.deleteAll();
            GameController.quit();
        }
    } else {
        settingsMenuLayout.setSpacing((int) Math.lerp(settingsMenuLayout.getSpacing(), -140, 25));
    }
    if (settingsBtn.isDown()) {
        toggleSettingsMenu = !toggleSettingsMenu;
    }
}
