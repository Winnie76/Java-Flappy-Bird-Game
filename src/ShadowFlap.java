import bagel.*;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;


public class ShadowFlap extends AbstractGame {
//    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final Image LEVEL0_BACKGROUND = new Image("res/level-0/background.png");
    private final Image LEVEL1_BACKGROUND = new Image("res/level-1/background.png");
    private final Image PLASTIC_PIPE = new Image("res/level/plasticPipe.png");
    private final Image STEEL_PIPE = new Image("res/level-1/steelPipe.png");
    private final Image ROCK = new Image("res/level-1/rock.png");
    private final Image BOMB = new Image("res/level-1/bomb.png");
    private final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final String SHOOT_MSG = "PRESS 'S' TO SHOOT";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private Bird bird;
    private LifeBar lifeBar;
//    private PipeSet pipeSet;
    private int score;
    private boolean gameOn;
    private boolean pipeCollision;
    private boolean win;
    private boolean isLevelUp = false;
    private ArrayList<PipeSet> plasticPipeSets = new ArrayList<>();
    private ArrayList<PipeSet> pipeSets = new ArrayList<>();
    private ArrayList<Weapon> weapons = new ArrayList<>();
    private int timeScale = 1;
    private final int MAX_SCALE = 5;
    private final int MIN_SCALE = 1;
    private int frameCount = 0;
    private final double SWITCH_FRAME = 200;
    private final double GENERATE_WEAPON = 270;
    private boolean levelUp = false;
    private int levelUpCount = 0;
    private boolean flameCollision = false;


    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new Bird();
        lifeBar = new LifeBar();
        score = 0;
        gameOn = false;
        pipeCollision = false;
        win = false;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        //draw background image
        if(!isLevelUp){
            LEVEL0_BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        }else{
            LEVEL1_BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // if game not started draw instruction screen
        if (!gameOn) {
            renderInstructionScreen(input);
        }
        // if no life left then game over
        if(lifeBar.getCurrentLife() == 0){
            renderGameOverScreen();
            resetGame();
        }
        // if bird out of bound then reduce life and reset bird height
        if(birdOutOfBound()){
            if(lifeBar.getCurrentLife() > 0){
                lifeBar.setCurrentLife(lifeBar.getCurrentLife() - 1);
                bird.setY((350));
            }
        }
        // if win then draw win screen
        if(win){
            renderWinScreen();
        }
        // if game is active + have life left + haven't won
        if(gameOn && !(lifeBar.getCurrentLife() == 0) && !win){
            // if haven't leveled up
            if(!levelUp){
                // check if timescale needs to be updated
                updateTimeScale(input);
                // add plastic pipesets every 100 frames
                if(frameCount % SWITCH_FRAME == 0){
                    plasticPipeSets.add(new PlasticPipeSet(PLASTIC_PIPE, isLevelUp));
                }
                // add frame count number
                frameCount ++;
                bird.update(input);
                Rectangle birdBox = bird.getBox();
                updatePipeSets(plasticPipeSets, birdBox);
                lifeBar.update();
            }
            // if not leveled up
            if(isLevelUp){
                // check the timescale
                updateTimeScale(input);
                // if framecount is 100 frame then add a random pipe
                if(frameCount % SWITCH_FRAME == 0){
                    randomAddPipe();
                }
                // if framecount is 110 then add a random weapon
                if(frameCount % GENERATE_WEAPON == 0){
                    randomAddWeapon();
                    weapons.get(weapons.size() - 1).setOverlap(overlapDetection(weapons.get(weapons.size() - 1).getBox(), pipeSets));
                }
                // add frame count
                frameCount += 1;
                // update bird if user presses space
                bird.update(input);
                Rectangle birdBox = bird.getBox();
                // check if bird collide with weapon or pipe
                updateWeapons(birdBox, input);
                updatePipeSets(pipeSets, birdBox);
                // update life bar
                lifeBar.update();
            }
        }
        // haven't been leveled up before & need to level up
        if (levelUp && !isLevelUp){
            renderLevelUpScreen();
            levelUpCount ++;
            if(levelUpCount == 150){
                resetGame();
            }
        }
    }

    public boolean birdOutOfBound() {
        return (bird.getY() > Window.getHeight()) || (bird.getY() < 0);
    }

    public boolean detectCollision(Rectangle birdBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        return birdBox.intersects(topPipeBox) ||
                birdBox.intersects(bottomPipeBox);
    }

    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }
    //
    public void renderLevelUpScreen() {
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    public void randomAddPipe(){
        Random rand = new Random();
        if (rand.nextBoolean()){
            pipeSets.add(new SteelPipeSet(STEEL_PIPE, isLevelUp));
        }else{
            pipeSets.add(new PlasticPipeSet(PLASTIC_PIPE, isLevelUp));
        }
    }

    public void randomAddWeapon(){
        Random rand = new Random();
        if (rand.nextBoolean()){
            weapons.add(new Rock(ROCK));
        }else{
            weapons.add(new Bomb(BOMB));
        }
    }

    public void updateTimeScale(Input input){
        if(input.wasPressed(Keys.L) && timeScale < MAX_SCALE){
            timeScale += 1;
        }
        if(input.wasPressed(Keys.K) && timeScale > MIN_SCALE){
            timeScale -= 1;
        }
    }

    public void renderInstructionScreen(Input input) {

        if(isLevelUp){
            FONT.drawString(SHOOT_MSG, (Window.getWidth()/2.0-(FONT.getWidth(SHOOT_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        }else{
            // paint the instruction on screen
            FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        }
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
    }
    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    public void updatePipeSets(ArrayList<PipeSet> pipeSets, Rectangle birdBox){
        for(PipeSet pipeSet: pipeSets){
            pipeSet.update(timeScale);
            Rectangle topPipeBox = pipeSet.getTopBox();
            Rectangle bottomPipeBox = pipeSet.getBottomBox();
            pipeCollision = detectCollision(birdBox, topPipeBox, bottomPipeBox);
            if(pipeCollision && !pipeSet.getIsCollide()){
                lifeBar.setCurrentLife(lifeBar.getCurrentLife() - 1);
                pipeSet.setIsCollide(true);
            }
            updateScore(pipeSet);
            if(pipeSet.getPipeImage().equals(STEEL_PIPE)){
                SteelPipeSet steelPipe = (SteelPipeSet) pipeSet;
                Rectangle topFlameBox = steelPipe.getTopFlame();
                Rectangle bottomFlameBox = steelPipe.getBottomFlame();
                if(steelPipe.getFrameCount() % 20 == 0){
                    flameCollision = detectCollision(birdBox, topFlameBox, bottomFlameBox);
                    if(flameCollision && !steelPipe.getCollisionWithFlame()){
                        lifeBar.setCurrentLife(lifeBar.getCurrentLife() - 1);
                        steelPipe.setCollideWithFlame(true);
                    }
                }
            }
        }


    }
    public void updateScore(PipeSet pipeSet) {
        if (bird.getX() > pipeSet.getTopBox().right() && !pipeSet.getIsPass()) {
            score += 1;
            pipeSet.setIsPass(true);
        }
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, 100, 100);

        // detect win
        if (score == 10 && !isLevelUp) {
            levelUp = true;
        }
        if (score == 30){
            win = true;
        }
    }
    public void resetGame(){
        isLevelUp = true;
        gameOn = false;
        bird.setLevelUp(true);
        timeScale = 1;
        lifeBar.setMaxLife(6);
        lifeBar.setCurrentLife(6);
        score = 0;
        frameCount = 0;
        bird.setY(350);
    }

    public boolean overlapDetection(Rectangle weaponBox, ArrayList<PipeSet> pipeSets){
        for (PipeSet pipe: pipeSets){
            Rectangle topPipeBox = pipe.getTopBox();
            Rectangle bottomPipeBox = pipe.getBottomBox();
            boolean overlap = detectCollision(weaponBox, topPipeBox, bottomPipeBox);
            if(overlap){
                return true;
            }
        }
        return false;
    }
    public void updateWeapons(Rectangle birdBox, Input input){
        for (Weapon weapon: weapons){
            Rectangle weaponBox = weapon.getBox();
            if(birdBox.intersects(weaponBox) && !weapon.getIsPickedUp() && !weapon.getOverlap()){
                if(!bird.getHoldWeapon()){
                    bird.setHoldWeapon(true);
                    weapon.setIsPickedUp(true);
                }
            }
            weapon.update(input, bird, timeScale);
            checkWeaponPipeCollision(weapon);
        }
    }
    public void checkWeaponPipeCollision(Weapon weapon){
        Rectangle weaponBox = weapon.getBox();
        for(PipeSet pipe: pipeSets){
            Rectangle topPipeBox = pipe.getTopBox();
            Rectangle bottomPipeBox = pipe.getBottomBox();
            if(!weapon.getDisappear() && weapon.getIsShoot()){
                boolean weaponCollision = detectCollision(weaponBox, topPipeBox, bottomPipeBox);
                if(weapon.getWeaponImage().equals(ROCK) && pipe.getPipeImage().equals(PLASTIC_PIPE) && weaponCollision){
                    weapon.setDisappear(true);
                    pipe.setIsCollide(true);
                    pipe.setIsPass(true);
                    score += 1;
                }
                if(weapon.getWeaponImage().equals(BOMB) && weaponCollision){
                    weapon.setDisappear(true);
                    pipe.setIsCollide(true);
                    pipe.setIsPass(true);
                    score += 1;
                }
            }

        }
    }

}
