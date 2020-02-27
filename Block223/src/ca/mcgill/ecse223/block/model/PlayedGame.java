/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.block.model;
import java.io.Serializable;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.model.BouncePoint.BounceDirection;
import java.util.*;

// line 17 "../../../../../Block223PlayMode.ump"
// line 71 "../../../../../Block223Persistence.ump"
// line 1 "../../../../../Block223States.ump"
public class PlayedGame implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------


  /**
   * at design time, the initial wait time may be adjusted as seen fit
   */
  public static final int INITIAL_WAIT_TIME = 90;

  private static int nextId = 1;
  public static final int NR_LIVES = 3;

  /**
   * the PlayedBall and PlayedPaddle are not in a separate class to avoid the bug in Umple that occurred for the second constructor of Game
   * no direct link to Ball, because the ball can be found by navigating to PlayedGame, Game, and then Ball
   */
  public static final int BALL_INITIAL_X = Game.PLAY_AREA_SIDE / 2;
  public static final int BALL_INITIAL_Y = Game.PLAY_AREA_SIDE / 2;

  /**
   * no direct link to Paddle, because the paddle can be found by navigating to PlayedGame, Game, and then Paddle
   * pixels moved when right arrow key is pressed
   */
  public static final int PADDLE_MOVE_RIGHT = 5;

  /**
   * pixels moved when left arrow key is pressed
   */
  public static final int PADDLE_MOVE_LEFT = -5;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PlayedGame Attributes
  private int score;
  private int lives;
  private int currentLevel;
  private double waitTime;
  private String playername;
  private double ballDirectionX;
  private double ballDirectionY;
  private double currentBallX;
  private double currentBallY;
  private double currentPaddleLength;
  private double currentPaddleX;
  private double currentPaddleY;

  //Autounique Attributes
  private int id;

  //PlayedGame State Machines
  public enum PlayStatus { Ready, Moving, Paused, GameOver }
  private PlayStatus playStatus;

  //PlayedGame Associations
  private Player player;
  private Game game;
  private List<PlayedBlockAssignment> blocks;
  private BouncePoint bounce;
  private Block223 block223;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PlayedGame(String aPlayername, Game aGame, Block223 aBlock223)
  {
    // line 53 "../../../../../Block223PlayMode.ump"
    boolean didAddGameResult = setGame(aGame);
          if (!didAddGameResult)
          {
             throw new RuntimeException("Unable to create playedGame due to game");
          }
    // END OF UMPLE BEFORE INJECTION
    score = 0;
    lives = NR_LIVES;
    currentLevel = 1;
    waitTime = INITIAL_WAIT_TIME;
    playername = aPlayername;
    resetBallDirectionX();
    resetBallDirectionY();
    resetCurrentBallX();
    resetCurrentBallY();
    currentPaddleLength = getGame().getPaddle().getMaxPaddleLength();
    resetCurrentPaddleX();
    currentPaddleY = Game.PLAY_AREA_SIDE - Paddle.VERTICAL_DISTANCE - Paddle.PADDLE_WIDTH;
    id = nextId++;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create playedGame due to game");
    }
    blocks = new ArrayList<PlayedBlockAssignment>();
    boolean didAddBlock223 = setBlock223(aBlock223);
    if (!didAddBlock223)
    {
      throw new RuntimeException("Unable to create playedGame due to block223");
    }
    setPlayStatus(PlayStatus.Ready);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setScore(int aScore)
  {
    boolean wasSet = false;
    score = aScore;
    wasSet = true;
    return wasSet;
  }

  public boolean setLives(int aLives)
  {
    boolean wasSet = false;
    lives = aLives;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentLevel(int aCurrentLevel)
  {
    boolean wasSet = false;
    currentLevel = aCurrentLevel;
    wasSet = true;
    return wasSet;
  }
  
  

  public boolean setWaitTime(double aWaitTime)
  {
    boolean wasSet = false;
    waitTime = aWaitTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setPlayername(String aPlayername)
  {
    boolean wasSet = false;
    playername = aPlayername;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setBallDirectionX(double aBallDirectionX)
  {
    boolean wasSet = false;
    ballDirectionX = aBallDirectionX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetBallDirectionX()
  {
    boolean wasReset = false;
    ballDirectionX = getDefaultBallDirectionX();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setBallDirectionY(double aBallDirectionY)
  {
    boolean wasSet = false;
    ballDirectionY = aBallDirectionY;
    wasSet = true;
    return wasSet;
  }

  public boolean resetBallDirectionY()
  {
    boolean wasReset = false;
    ballDirectionY = getDefaultBallDirectionY();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentBallX(double aCurrentBallX)
  {
    boolean wasSet = false;
    currentBallX = aCurrentBallX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentBallX()
  {
    boolean wasReset = false;
    currentBallX = getDefaultCurrentBallX();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentBallY(double aCurrentBallY)
  {
    boolean wasSet = false;
    currentBallY = aCurrentBallY;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentBallY()
  {
    boolean wasReset = false;
    currentBallY = getDefaultCurrentBallY();
    wasReset = true;
    return wasReset;
  }

  public boolean setCurrentPaddleLength(double aCurrentPaddleLength)
  {
    boolean wasSet = false;
    currentPaddleLength = aCurrentPaddleLength;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentPaddleX(double aCurrentPaddleX)
  {
    boolean wasSet = false;
	//currentPaddleX = aCurrentPaddleX;
    if (aCurrentPaddleX < 0) {
      currentPaddleX = 0;
      wasSet = true;
    } else if (aCurrentPaddleX > 370) {
      currentPaddleX = 370; // TODO i think gunter said to change this value??
      wasSet = true;
    } else {
      currentPaddleX = aCurrentPaddleX;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean resetCurrentPaddleX()
  {
    boolean wasReset = false;
    currentPaddleX = getDefaultCurrentPaddleX();
    wasReset = true;
    return wasReset;
  }

  public int getScore()
  {
    return score;
  }

  public int getLives()
  {
    return lives;
  }

  public int getCurrentLevel()
  {
    return currentLevel;
  }

  public double getWaitTime()
  {
    return waitTime;
  }

  /**
   * added here so that it only needs to be determined once
   */
  public String getPlayername()
  {
    return playername;
  }

  /**
   * 0/0 is the top left corner of the play area, i.e., a directionX/Y of 0/1 moves the ball down in a straight line
   */
  public double getBallDirectionX()
  {
    return ballDirectionX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultBallDirectionX()
  {
    return getGame().getBall().getMinBallSpeedX();
  }

  public double getBallDirectionY()
  {
    return ballDirectionY;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultBallDirectionY()
  {
    return getGame().getBall().getMinBallSpeedY();
  }

  /**
   * the position of the ball is at the center of the ball
   */
  public double getCurrentBallX()
  {
    return currentBallX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentBallX()
  {
    return BALL_INITIAL_X;
  }

  public double getCurrentBallY()
  {
    return currentBallY;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentBallY()
  {
    return BALL_INITIAL_Y;
  }

  public double getCurrentPaddleLength()
  {
    return currentPaddleLength;
  }

  /**
   * the position of the paddle is at its top right corner
   */
  public double getCurrentPaddleX()
  {
    return currentPaddleX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentPaddleX()
  {
    return (Game.PLAY_AREA_SIDE - currentPaddleLength) / 2;
  }

  public double getCurrentPaddleY()
  {
    return currentPaddleY;
  }

  public int getId()
  {
    return id;
  }

  public String getPlayStatusFullName()
  {
    String answer = playStatus.toString();
    return answer;
  }

  public PlayStatus getPlayStatus()
  {
    return playStatus;
  }

  public boolean play()
  {
    boolean wasEventProcessed = false;
    
    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Ready:
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      case Paused:
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean pause()
  {
    boolean wasEventProcessed = false;
    
    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Moving:
        setPlayStatus(PlayStatus.Paused);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean move()
  {
    boolean wasEventProcessed = false;
    
    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Moving:
        if (hitPaddle())
        {
        // line 17 "../../../../../Block223States.ump"
          doHitPaddleOrWall();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        if (isOutOfBoundsAndLastLife())
        {
        // line 18 "../../../../../Block223States.ump"
          doOutOfBounds();
          setPlayStatus(PlayStatus.GameOver);
          wasEventProcessed = true;
          break;
        }
        if (isOutOfBounds())
        {
        // line 19 "../../../../../Block223States.ump"
          doOutOfBounds();
          setPlayStatus(PlayStatus.Paused);
          wasEventProcessed = true;
          break;
        }
        if (hitLastBlockAndLastLevel())
        {
        // line 20 "../../../../../Block223States.ump"
          doHitBlock();
          PlayedBlockAssignment pblock = bounce.getHitBlock();
	   	  Block block = pblock.getBlock();
	   	  int points = block.getPoints();
	   	  this.setScore(this.getScore() + points);
          setPlayStatus(PlayStatus.GameOver);
          wasEventProcessed = true;
          break;
        }
        if (hitLastBlock())
        {
        // line 21 "../../../../../Block223States.ump"
          PlayedBlockAssignment pblock = bounce.getHitBlock();
 	      Block block = pblock.getBlock();
 	      int points = block.getPoints();
 	   	  this.setScore(this.getScore() + points);
          doHitBlockNextLevel();
          setPlayStatus(PlayStatus.Ready);
          wasEventProcessed = true;
          break;
        }
        if (hitBlock())
        {
        // line 22 "../../../../../Block223States.ump"
          doHitBlock();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        if (hitWall())
        {
        // line 23 "../../../../../Block223States.ump"
          doHitPaddleOrWall();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        // line 24 "../../../../../Block223States.ump"
        doHitNothingAndNotOutOfBounds();
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setPlayStatus(PlayStatus aPlayStatus)
  {
    playStatus = aPlayStatus;

    // entry actions and do activities
    switch(playStatus)
    {
      case Ready:
        // line 12 "../../../../../Block223States.ump"
        doSetup();
        break;
      case GameOver:
        // line 30 "../../../../../Block223States.ump"
        doGameOver();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public boolean hasPlayer()
  {
    boolean has = player != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetMany */
  public PlayedBlockAssignment getBlock(int index)
  {
    PlayedBlockAssignment aBlock = blocks.get(index);
    return aBlock;
  }

  public List<PlayedBlockAssignment> getBlocks()
  {
    List<PlayedBlockAssignment> newBlocks = Collections.unmodifiableList(blocks);
    return newBlocks;
  }

  public int numberOfBlocks()
  {
    int number = blocks.size();
    return number;
  }

  public boolean hasBlocks()
  {
    boolean has = blocks.size() > 0;
    return has;
  }

  public int indexOfBlock(PlayedBlockAssignment aBlock)
  {
    int index = blocks.indexOf(aBlock);
    return index;
  }
  /* Code from template association_GetOne */
  public BouncePoint getBounce()
  {
    return bounce;
  }

  public boolean hasBounce()
  {
    boolean has = bounce != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Block223 getBlock223()
  {
    return block223;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setPlayer(Player aPlayer)
  {
    boolean wasSet = false;
    Player existingPlayer = player;
    player = aPlayer;
    if (existingPlayer != null && !existingPlayer.equals(aPlayer))
    {
      existingPlayer.removePlayedGame(this);
    }
    if (aPlayer != null)
    {
      aPlayer.addPlayedGame(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removePlayedGame(this);
    }
    game.addPlayedGame(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBlocks()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public PlayedBlockAssignment addBlock(int aX, int aY, Block aBlock)
  {
    return new PlayedBlockAssignment(aX, aY, aBlock, this);
  }

  public boolean addBlock(PlayedBlockAssignment aBlock)
  {
    boolean wasAdded = false;
    if (blocks.contains(aBlock)) { return false; }
    PlayedGame existingPlayedGame = aBlock.getPlayedGame();
    boolean isNewPlayedGame = existingPlayedGame != null && !this.equals(existingPlayedGame);
    if (isNewPlayedGame)
    {
      aBlock.setPlayedGame(this);
    }
    else
    {
      blocks.add(aBlock);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBlock(PlayedBlockAssignment aBlock)
  {
    boolean wasRemoved = false;
    //Unable to remove aBlock, as it must always have a playedGame
    if (!this.equals(aBlock.getPlayedGame()))
    {
      blocks.remove(aBlock);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBlockAt(PlayedBlockAssignment aBlock, int index)
  {  
    boolean wasAdded = false;
    if(addBlock(aBlock))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBlocks()) { index = numberOfBlocks() - 1; }
      blocks.remove(aBlock);
      blocks.add(index, aBlock);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBlockAt(PlayedBlockAssignment aBlock, int index)
  {
    boolean wasAdded = false;
    if(blocks.contains(aBlock))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBlocks()) { index = numberOfBlocks() - 1; }
      blocks.remove(aBlock);
      blocks.add(index, aBlock);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBlockAt(aBlock, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setBounce(BouncePoint aNewBounce)
  {
    boolean wasSet = false;
    bounce = aNewBounce;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBlock223(Block223 aBlock223)
  {
    boolean wasSet = false;
    if (aBlock223 == null)
    {
      return wasSet;
    }

    Block223 existingBlock223 = block223;
    block223 = aBlock223;
    if (existingBlock223 != null && !existingBlock223.equals(aBlock223))
    {
      existingBlock223.removePlayedGame(this);
    }
    block223.addPlayedGame(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (player != null)
    {
      Player placeholderPlayer = player;
      this.player = null;
      placeholderPlayer.removePlayedGame(this);
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePlayedGame(this);
    }
    while (blocks.size() > 0)
    {
      PlayedBlockAssignment aBlock = blocks.get(blocks.size() - 1);
      aBlock.delete();
      blocks.remove(aBlock);
    }
    
    bounce = null;
    Block223 placeholderBlock223 = block223;
    this.block223 = null;
    if(placeholderBlock223 != null)
    {
      placeholderBlock223.removePlayedGame(this);
    }
  }


  /**
   * Guards
   * 
   * Feature 3 : hitPaddle()
   * @author: Walid Chabchoub
   * 
   */
  // line 43 "../../../../../Block223States.ump"
  private boolean hitPaddle(){
	  BouncePoint bp = calculateBouncePointPaddle();
	  if(bp!= null) {
		  setBounce(bp);
		  return true;
	  }else {
		  return false;
	  }
  }


  /**
   * 
   * Helper method calculate BouncePoint Paddle.
   * 
   * Feature 3 : calculateBouncePointPaddle()
   * @author: Walid Chabchoub
   * 
   */
  // line 65 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointPaddle(){
    /*
		 * Setting Rectangle2D(x, y, width, height) with x/y being at top-left of rectangle
		 * In Java AWT, height is the width in the Iter 4 sample sol
		 * and width is the length of box A 
		 */
	   double ballX = this.getCurrentBallX();
	   double ballY = this.getCurrentBallY();
	   double ballDirX = this.getBallDirectionX();
	   double ballDirY = this.getBallDirectionY();

		// Ball related variables 
		double ballRadius = Ball.BALL_DIAMETER/2;
		Line2D ballTrajectory = new Line2D.Double(ballX, ballY, ballX+ballDirX, ballY+ballDirY);

		// Paddle stuff
		double paddleX = this.getCurrentPaddleX();
		double paddleY = this.getCurrentPaddleY();
		double paddleLength = this.getCurrentPaddleLength();
		double paddleWidth = (double) Paddle.PADDLE_WIDTH;

		// Defining our boxes A, B, C, E, F
		Rectangle2D fullBox = new Rectangle2D.Double(paddleX - ballRadius, paddleY - ballRadius,
				paddleLength+2*ballRadius, 2*ballRadius);
		Rectangle2D boxA = new Rectangle2D.Double(paddleX, (paddleY)-ballRadius,
				paddleLength, ballRadius);
		Rectangle2D boxB = new Rectangle2D.Double(paddleX - ballRadius, paddleY,
				ballRadius, ballRadius);
		Rectangle2D boxC = new Rectangle2D.Double(paddleX + paddleLength, paddleY,
				ballRadius, ballRadius);
		Rectangle2D boxE = new Rectangle2D.Double(paddleX - ballRadius, paddleY - ballRadius,
				ballRadius, ballRadius);
		Rectangle2D boxF = new Rectangle2D.Double(paddleX + paddleLength, paddleY - ballRadius,
				ballRadius, ballRadius);
		
		// Checking first if the ball is even going to hit the paddle at all
		if(fullBox.intersectsLine(ballTrajectory)) {
			//Next checking each box hit
			if(boxA.intersectsLine(ballTrajectory)) {
				//yellowLineA here is the top of box A where the ball is going to hit
				Line2D yellowLineA = new Line2D.Double(boxA.getX(), boxA.getY(), boxA.getX()+boxA.getWidth(), boxA.getY());
				Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, yellowLineA);
				return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);
			}else if(boxB.intersectsLine(ballTrajectory)) {
				//yellowLineB here is the left of box B where the ball is going to hit
				Line2D yellowLineB = new Line2D.Double(boxB.getX(), boxB.getY(), boxB.getX(), boxB.getY()+ballRadius);
				Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, yellowLineB);
				return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);
			}else if(boxC.intersectsLine(ballTrajectory)) {
				//yellowLineC here is the right of box C where the ball is going to hit
				Line2D yellowLineC = new Line2D.Double(boxC.getX()+ballRadius, boxC.getY(), boxC.getX()+ballRadius, boxC.getY()+ballRadius);
				Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, yellowLineC);
				return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);			
			}else if(boxE.intersectsLine(ballTrajectory)) {
				// Will be treating the problem as a full circle instead of quarter circle segment
				// Center of circle is conveniently next to box B (x, y) coordinates being at the top-left corner (adding a ball radius to x)
				Point2D circleECenter = new Point2D.Double(boxB.getX()+ballRadius, boxB.getY());
				Point2D intersectionPoint = getCircleLineIntersectionPoint(ballTrajectory.getP1(), ballTrajectory.getP2(), circleECenter, ballRadius);
				// If ball approaches from left
				if(ballX < ballX + ballDirX) {
					return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);
				}
				// If ball approaches from right
				else{
					return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);
				}	
			}else if(boxF.intersectsLine(ballTrajectory)) {
				// Will be treating the problem as a full circle instead of quarter circle segment
				// Center of circle is conveniently at box C (x, y) coordinates being at the top-left corner
				Point2D circleFCenter = new Point2D.Double(boxC.getX(), boxC.getY());
				Point2D intersectionPoint = getCircleLineIntersectionPoint(ballTrajectory.getP1(), ballTrajectory.getP2(), circleFCenter, ballRadius);
				// If ball approaches from left
				if(ballX < ballX + ballDirX) {
					return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);
				}
				// If ball approaches from right
				else{
					return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);
				}
			}
			//unlikely that this will be reached (if it is, then problem is with boxes E/F)
			return null;
		} else {
		  
			return null;
		}
  }


  /**
   * 
   * Helper method calculate intersection point for round edges (box E/F for paddle).
   * 
   * Feature 3 : getCircleLineIntersectionPoint()
   * @author: Walid Chabchoub
   * 
   */
  // line 153 "../../../../../Block223States.ump"
   public Point2D getCircleLineIntersectionPoint(Point2D pointA, Point2D pointB, Point2D center, double radius){
        double baX = pointB.getX() - pointA.getX();
        double baY = pointB.getY() - pointA.getY();
        double caX = center.getX() - pointA.getX();
        double caY = center.getY() - pointA.getY();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return null;
        }
        // if disc == 0 ... dealt with later
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Point2D p1 = new Point2D.Double(pointA.getX() - baX * abScalingFactor1, pointA.getY() - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return p1;
        }
        Point2D p2 = new Point2D.Double(pointA.getX() - baX * abScalingFactor2, pointA.getY() - baY * abScalingFactor2);
        
        if(pointA.distanceSq(p1) < pointA.distanceSq(p2)) {
        	return p1;
        }else {
        	return p2;
        }
  }


  /**
   * used by calculateBouncePointPaddle & calculateBouncePointWall & calculateBouncePointBlock
   */
  // line 190 "../../../../../Block223States.ump"
   public Point2D getIntersectionPoint(Line2D lineA, Line2D lineB){
        double x1 = lineA.getX1();
        double y1 = lineA.getY1();
        double x2 = lineA.getX2();
        double y2 = lineA.getY2();

        double x3 = lineB.getX1();
        double y3 = lineB.getY1();
        double x4 = lineB.getX2();
        double y4 = lineB.getY2();

        Point2D p = null;

        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d != 0) {
            double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
            double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

            p = new Point2D.Double(xi, yi);

        }
        return p;
  }

   /*
    * 
    * Feature 5: isBallOutOfBounds()
    * @author: Trevor Tabah
    * 
    */
   private boolean isBallOutOfBounds() {
	   //radius of ball
	   double ballRadius = Ball.BALL_DIAMETER/2;
	   //current position and direction to find where ball will be
	   double ballX = this.getCurrentBallX();
	   double ballY = this.getCurrentBallY();
	   double ballDirX = this.getBallDirectionX();
	   double ballDirY = this.getBallDirectionY();
	   //finding the trajectory of the ball
	   Line2D ballTrajectory = new Line2D.Double(ballX, ballY, ballX+ballDirX, ballY+ballDirY);
	   //Top corners of Box D
	   Point2D boxDTopLeftCorner = new Point2D.Double(Game.PLAY_AREA_SIDE, Game.PLAY_AREA_SIDE-ballRadius);
	   Point2D boxDTopRightCorner = new Point2D.Double(0, Game.PLAY_AREA_SIDE-ballRadius);
	   //Line segment of the top of BoxD
	   Line2D boxDLine = new Line2D.Double(boxDTopLeftCorner, boxDTopRightCorner);
	   // returns true if ball will intersect top of boxD which means ball is out of bounds
	   return (ballTrajectory.intersectsLine(boxDLine));
   }
   
// line 214 "../../../../../Block223States.ump"
	/*
	 * 
	 * Feature 5: isOutOfBoundsAndLastLife()
	 * @author: Trevor Tabah
	 * 
	 */
   private boolean isOutOfBoundsAndLastLife(){
	   boolean outOfBounds = false;
	   if (lives == 1) {
		   outOfBounds = isBallOutOfBounds();
	   }
	   return outOfBounds;
  }

  // line 219 "../../../../../Block223States.ump"
// line 219 "../../../../../Block223States.ump"
   /*
    * Feature 5: isOutOfBounds()
    * @author: Trevor Tabah
    */
   private boolean isOutOfBounds(){
	   boolean outOfBounds = isBallOutOfBounds();
	   return outOfBounds;
  }


	
   
   /*
    * Feature 4 hitLastBlockAndLastLevel()
    * @author: Jeremy Chow
    */

  // line 224 "../../../../../Block223States.ump"
   private boolean hitLastBlockAndLastLevel(){
	   Game game = this.getGame();
	   int nRlevels = game.numberOfLevels();
	   this.setBounce(null);
	   if ((nRlevels) == this.currentLevel) {
		   int nrBlocks = this.numberOfBlocks();
		   if (nrBlocks == 1) {
			   PlayedBlockAssignment block = this.getBlock(0);
			   BouncePoint bp = calculateBouncePointBlock(block);
			   this.setBounce(bp); // returns null if no BouncePoint found
			   return (bp != null);
		   }
	   }
	   
    return false;
  }
   
   /*
    * Feature 4 calculateBouncePointBlock()
    * @author: Jeremy Chow
    * Created boxes around each block, if the ball trajectory intersects with the block, make a line (circle for corners) to find
    * the intersection point then return the intersection point and which direction to flip.
    */
   
  private BouncePoint calculateBouncePointBlock(PlayedBlockAssignment block) {
	  double ballradius = Ball.BALL_DIAMETER/2;
	  double ballX = this.getCurrentBallX();
	  double ballY = this.getCurrentBallY();
	  double ballDirX = this.getBallDirectionX();
	  double ballDirY = this.getBallDirectionY();
	  
	  Line2D ballTrajectory = new Line2D.Double(ballX, ballY, ballX+ballDirX, ballY+ballDirY);
	  
	  // initializing boxes
	  // sides
	  Rectangle2D boxA = new Rectangle2D.Double(block.getX(), block.getY() - ballradius, Block.SIZE, ballradius); // top
	  Rectangle2D boxB = new Rectangle2D.Double(block.getX() - ballradius, block.getY(), ballradius, Block.SIZE); // left
	  Rectangle2D boxC = new Rectangle2D.Double(block.getX() + Block.SIZE, block.getY(), ballradius, Block.SIZE); // right
	  Rectangle2D boxD = new Rectangle2D.Double(block.getX(), block.getY() + Block.SIZE, Block.SIZE, ballradius); // bottom
	  // corners
	  Rectangle2D boxE = new Rectangle2D.Double(block.getX() - ballradius, block.getY() - ballradius, ballradius, ballradius); // topleft
	  Rectangle2D boxF = new Rectangle2D.Double(block.getX() + Block.SIZE, block.getY() - ballradius, ballradius, ballradius); // topright
	  Rectangle2D boxG = new Rectangle2D.Double(block.getX() - ballradius, block.getY() + Block.SIZE, ballradius, ballradius); // bottomleft
	  Rectangle2D boxH = new Rectangle2D.Double(block.getX() + Block.SIZE, block.getY() + Block.SIZE, ballradius, ballradius); // bottomright
	  
	  // sides
	  if (boxA.intersectsLine(ballTrajectory)) {
		  Line2D intersectionLine = new Line2D.Double(boxA.getX(), boxA.getY(), boxA.getMaxX(), boxA.getMinY());
		  Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, intersectionLine);
		  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);
		  returnBp.setHitBlock(block);
		  return returnBp;
		  
	  }
	  else if (boxB.intersectsLine(ballTrajectory)) {
		  Line2D intersectionLine = new Line2D.Double(boxB.getX(), boxB.getY(), boxB.getMinX(), boxB.getMaxY());
		  Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, intersectionLine);
		  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);
		  returnBp.setHitBlock(block);
		  return returnBp;
	  }
	  else if (boxC.intersectsLine(ballTrajectory)) {
		  Line2D intersectionLine = new Line2D.Double(boxC.getX() + ballradius, boxC.getY(), boxC.getMaxX(), boxC.getMaxY());
		  Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, intersectionLine);
		  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);
		  returnBp.setHitBlock(block);
		  return returnBp;
	  }
	  else if (boxD.intersectsLine(ballTrajectory)) {
		  Line2D intersectionLine = new Line2D.Double(boxD.getX(), boxD.getY() + ballradius, boxD.getMaxX(), boxD.getMaxY());
		  Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, intersectionLine);
		  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);
		  returnBp.setHitBlock(block);
		  return returnBp;
	  }
	  // corners (getting to this point means only corner rectangle was intersected
	  // uses circle as corner instead of arc
	  // coming from left or right determines flip direction since non-corner case intersections are aleady checked above

//	  else if (boxE.intersectsLine(ballTrajectory) && (ballTrajectory.getP1().getX() > ballTrajectory.getP2().getX() ||
//			  ballTrajectory.getP1().getY() > ballTrajectory.getP2().getY())) {
	  else if (boxE.intersectsLine(ballTrajectory) && ((ballDirX > 0) || (ballDirY > 0))) {

		  Point2D circleCenter = new Point2D.Double(boxE.getX() + boxE.getWidth(), boxE.getY() + boxE.getHeight());
		  Point2D intersectionPoint = getCircleLineIntersectionPoint(ballTrajectory.getP1(), ballTrajectory.getP2(), circleCenter, ballradius);
		  if (ballDirX > 0) { // approaching from left
			  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);
			  returnBp.setHitBlock(block);
			  return returnBp;
		  }
		  else {
			  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);
			  returnBp.setHitBlock(block);
			  return returnBp;
		  }
	  }
	  else if (boxF.intersectsLine(ballTrajectory)) {
		  Point2D circleCenter = new Point2D.Double(boxF.getX(), boxF.getY() + boxE.getHeight());
		  Point2D intersectionPoint = getCircleLineIntersectionPoint(ballTrajectory.getP1(), ballTrajectory.getP2(), circleCenter, ballradius);
		  if (ballDirX > 0) {
			  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);
			  returnBp.setHitBlock(block);
			  return returnBp;
		  }
		  else {
			  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);
			  returnBp.setHitBlock(block);
			  return returnBp;
		  }
		  
	  }
	  else if (boxG.intersectsLine(ballTrajectory)) {
		  Point2D circleCenter = new Point2D.Double(boxG.getX() + boxG.getWidth(), boxG.getY());
		  Point2D intersectionPoint = getCircleLineIntersectionPoint(ballTrajectory.getP1(), ballTrajectory.getP2(), circleCenter, ballradius);
		  if (ballDirX > 0) {
			  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);
			  returnBp.setHitBlock(block);
			  return returnBp;
		  }
		  else {
			  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);
			  returnBp.setHitBlock(block);
			  return returnBp;
		  }
	  }
	  else if (boxH.intersectsLine(ballTrajectory)) {
		  Point2D circleCenter = new Point2D.Double(boxH.getX(), boxH.getY());
		  Point2D intersectionPoint = getCircleLineIntersectionPoint(ballTrajectory.getP1(), ballTrajectory.getP2(), circleCenter, ballradius);
		  if (ballDirX > 0) {
			  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);
			  returnBp.setHitBlock(block);
			  return returnBp;
		  }
		  else {
			  BouncePoint returnBp = new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);
			  returnBp.setHitBlock(block);
			  return returnBp;
		  }
	  }
	  else return null;
	  
	  
  }
  /*
   * Feature 4 hitLastBlock()
   * @author: Jeremy Chow
   */
  // line 229 "../../../../../Block223States.ump"
   private boolean hitLastBlock(){
	   int nrBlocks = this.numberOfBlocks();
	   this.setBounce(null);
	   if (nrBlocks == 1) {
		   PlayedBlockAssignment block = this.getBlock(0);
		   BouncePoint bp = this.calculateBouncePointBlock(block);
		   this.setBounce(bp);
		   return (bp != null);
				  
	   }
    return false;
  }
   /*
    * Feature 4 hitBlock()
    * @author: Jeremy Chow
    */
  // line 234 "../../../../../Block223States.ump"
   private boolean hitBlock(){
	   int nrBlocks = this.numberOfBlocks();
	   this.setBounce(null);
	   for (int i = 0;i < nrBlocks; i++) {
		   PlayedBlockAssignment block = this.getBlock(i);
		   if(block != null) {
			   BouncePoint bp = this.calculateBouncePointBlock(block);
			   BouncePoint bounce = this.getBounce(); // currently defined bouncepoint (will be replaced if a closer one is found)
			   boolean closer = isCloser(bp, bounce);// if two blocks are registered as hit, pick the closer one
			   if (closer) {
				   this.setBounce(bp);
			   }
		   }
	   }
    return (this.getBounce() != null);
  }
   /*
    * Feature 4 isCloser()
    * @author: Jeremy Chow
    * Checks which BouncePoint is closer if two blocks are registered as hit
    */
  private boolean isCloser(BouncePoint first, BouncePoint second) {
	  if (second == null) {
		  return true;
	  }
	  if (first == null) {
		  return false;
	  }
	  double distanceFirst = Point2D.distance(this.getCurrentBallX(), this.getCurrentBallY(), first.getX(), first.getY());
	  double distanceSecond = Point2D.distance(this.getCurrentBallX(), this.getCurrentBallY(), second.getX(), second.getY());
	  if (distanceFirst < distanceSecond) {
		  return true;
	  }
	  else return false;
  }

  /**
   * 
   * Feature 3 : hitWall()
   * @author: Walid Chabchoub
   * 
   */
  // line 245 "../../../../../Block223States.ump"
  private boolean hitWall(){


	  BouncePoint bp = calculateBouncePointWall();
	  if(bp!= null) {
		  setBounce(bp);
		  return true;
	  }else {
		  return false;
	  }
  }


  /**
   * 
   * Feature 3 : calculateBouncePointWall()
   * @author: Walid Chabchoub
   * 
   */
  // line 265 "../../../../../Block223States.ump"
  private BouncePoint calculateBouncePointWall(){
	  /*
	   * Setting Rectangle2D(x, y, width, height) with x/y being at top-left of rectangle
	   * In Java AWT, height is the width in the Iter 4 sample sol
	   * and width is the length of box A 
	   */
	  double ballX = this.getCurrentBallX();
	  double ballY = this.getCurrentBallY();
	  double ballDirX = this.getBallDirectionX();
	  double ballDirY = this.getBallDirectionY();

	  // Ball related variables 
	  double ballRadius = Ball.BALL_DIAMETER/2;
	  Line2D ballTrajectory = new Line2D.Double(ballX, ballY, ballX+ballDirX, ballY+ballDirY);


	  // Defining our boxes A, B, C, D

		Rectangle2D boxA = new Rectangle2D.Double(0, 0, ballRadius, Game.PLAY_AREA_SIDE-ballRadius);
		Rectangle2D boxB = new Rectangle2D.Double(ballRadius, 0, Game.PLAY_AREA_SIDE-2*ballRadius, ballRadius);
		Rectangle2D boxC = new Rectangle2D.Double(Game.PLAY_AREA_SIDE-ballRadius, 0, ballRadius, Game.PLAY_AREA_SIDE-ballRadius);
		//boxD not really used here, just for coordinates
		Rectangle2D boxD = new Rectangle2D.Double(0, Game.PLAY_AREA_SIDE-ballRadius, Game.PLAY_AREA_SIDE, ballRadius);
		
		if(boxA.intersectsLine(ballTrajectory)) {
			//yellowLineA here is the right of box A where the ball is going to hit
			Line2D yellowLineA = new Line2D.Double(ballRadius, ballRadius, ballRadius, boxD.getY());
			Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, yellowLineA);
			// Checking if hit is at the corner of A & B, then need to flip both (corner case)
			if((new Point2D.Double(ballX, ballY)).equals(yellowLineA.getP1())) {
				return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_BOTH);		
			}else {
				return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);		
			}
		}else if(boxB.intersectsLine(ballTrajectory)) {
			//yellowLineB here is the bottom of box B where the ball is going to hit
			Line2D yellowLineB = new Line2D.Double(ballRadius, ballRadius, boxC.getX(), ballRadius);
			Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, yellowLineB);
			// Checking if hit is at the corner of B, then need to flip both (corner case)
			if((new Point2D.Double(ballX, ballY)).equals(yellowLineB.getP2()) || (new Point2D.Double(ballX, ballY)).equals(yellowLineB.getP1())) {
				return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_BOTH);		
			} else {
				return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_Y);		
			}
		}else if(boxC.intersectsLine(ballTrajectory)) {
			//yellowLineC here is the right of box A where the ball is going to hit
			Line2D yellowLineC = new Line2D.Double(boxC.getX(), ballRadius, boxC.getX(), boxD.getY());
			Point2D intersectionPoint = getIntersectionPoint(ballTrajectory, yellowLineC);
			// Checking if hit is at the corner of A & B, then need to flip both (corner case)
			if((new Point2D.Double(ballX, ballY)).equals(yellowLineC.getP1())) {
				return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_BOTH);		
			}else {
				return new BouncePoint(intersectionPoint.getX(), intersectionPoint.getY(), BounceDirection.FLIP_X);		
			}
		}else {
			return null;
		}
  }


  /**
   * Actions
   */
  // line 322 "../../../../../Block223States.ump"
   private void doSetup(){
	    resetCurrentBallX();
	    resetCurrentBallY();
	    resetBallDirectionX();
	    resetBallDirectionY();
	    resetCurrentPaddleX();
	    Game game = getGame();
	    Level level = game.getLevel(currentLevel - 1);
	    List<BlockAssignment> assignments = level.getBlockAssignments();
	    // create block assignments
	    for (BlockAssignment a : assignments) {
	      PlayedBlockAssignment pblock = new PlayedBlockAssignment(
	          ( Game.WALL_PADDING + ( ( Block.SIZE + Game.COLUMNS_PADDING ) * ( a.getGridHorizontalPosition() - 1 ) ) ),
	          ( Game.WALL_PADDING + ( ( Block.SIZE + Game.ROW_PADDING ) * ( a.getGridVerticalPosition() - 1 ) ) ),
	          a.getBlock(),
	          this
	          );
	      /* TODO do we have to do anything with pblock to add it to a game */
	    }
//	     add random blocks to fill game level
	    while (numberOfBlocks() < game.getNrBlocksPerLevel()) {
	      Random r = new Random();
	      // find random x and y for new block
	      // can fit 14 blocks width wise (max is like 14.8)
	      // can fit 16 blocks height wise (max is like 16.8)
	      int x = r.nextInt(15);
	      int y = r.nextInt(17);
	      PlayedBlockAssignment pblock = new PlayedBlockAssignment(
	          ( Game.WALL_PADDING + ( ( Block.SIZE + Game.COLUMNS_PADDING ) * ( x ) ) ), 
	          ( Game.WALL_PADDING + ( ( Block.SIZE + Game.ROW_PADDING ) * ( y ) ) ), 
	          game.getRandomBlock(), this);
	      /* TODO do we have to do anything with pblock to add it to a game */
	    }
  }


  /**
   * 
   * Feature 3 : doHitPaddleOrWall()
   * @author: Walid Chabchoub
   * 
   */
  // line 363 "../../../../../Block223States.ump"
   private void doHitPaddleOrWall(){
    BouncePoint bp = this.getBounce();
		double newX, newY, newDirX, newDirY;
		
		
		double incomingDistanceX = bp.getX() - this.getCurrentBallX();
		double incomingDistanceY = bp.getY() - this.getCurrentBallY();

		double outgoingDistanceX = this.getBallDirectionX() - incomingDistanceX;
		double outgoingDistanceY = this.getBallDirectionY() - incomingDistanceY;
		
		if(outgoingDistanceY == 0.0) {
			newX = bp.getX();
			newY = bp.getY();
			newDirX = this.getBallDirectionX();
			newDirY = this.getBallDirectionY();
		} else {
			// Flipping only X
			if(bp.getDirection() == BounceDirection.FLIP_X) {
				newDirX = -1*this.getBallDirectionX();
				if(this.getBallDirectionY()<0) {
					newDirY = this.getBallDirectionY() - 0.1*Math.abs(getBallDirectionX());
				}else {
					newDirY = this.getBallDirectionY() + 0.1*Math.abs(getBallDirectionX());
				}
				newX = bp.getX() + newDirX;
				newY = bp.getY() + newDirY;
			// Flipping only Y
			}else if(bp.getDirection() == BounceDirection.FLIP_Y) {
				newDirY = -1*this.getBallDirectionY();
				if(this.getBallDirectionX()<0) {
					newDirX = this.getBallDirectionX() - 0.1*Math.abs(getBallDirectionY());
				}else {
					newDirX = this.getBallDirectionX() + 0.1*Math.abs(getBallDirectionY());
				}
				newX = bp.getX() + newDirX;
				newY = bp.getY() + newDirY;
			}
			// If we're flipping both X & Y
			else {
				newDirX = -1*this.getBallDirectionX();
				newDirY = -1*this.getBallDirectionY();
				newX = bp.getX() + newDirX;
				newY = bp.getY() + newDirY;
			}
		}
		this.setCurrentBallX(newX);
		this.setCurrentBallY(newY);
		this.setBallDirectionX(newDirX);
		this.setBallDirectionY(newDirY);
  }

   // line 402 "../../../../../Block223States.ump"
   /*
    * Feature 5: doOutOfBounds()
    * @author: Trevor Tabah
    */
   private void doOutOfBounds(){
	   setLives(lives-1);
	   resetBallDirectionX();
	   resetBallDirectionY();
	   resetCurrentBallX();
	   resetCurrentBallY();
	   resetCurrentPaddleX();

  }


	  
   /*
    * Feature 4 doHitBLock()
    * @author: Jeremy Chow
    */
   // line 406 "../../../../../Block223States.ump"
   private void doHitBlock(){
	   BouncePoint bp = this.getBounce();
	   double newX = 0, newY = 0, newDirX = 0, newDirY = 0;
	   double incomingDistanceX = bp.getX() - this.getCurrentBallX();
	   double incomingDistanceY = bp.getY() - this.getCurrentBallY();

	   double outgoingDistanceX = this.getBallDirectionX() - incomingDistanceX;
	   double outgoingDistanceY = this.getBallDirectionY() - incomingDistanceY;
	   PlayedBlockAssignment pblock = bounce.getHitBlock();
	   Block block = pblock.getBlock();
	   int points = block.getPoints();
	   //this.setScore(this.getScore() + points);
	   if(outgoingDistanceX == 0.0 || outgoingDistanceY == 0.0) {
		   newX = bp.getX();
		   newY = bp.getY();
		   newDirX = this.getBallDirectionX();
		   newDirY = this.getBallDirectionY();
	   }else {
		   this.setScore(this.getScore() + points);
		   pblock.delete();
		   // Flipping only X
		   if(bp.getDirection() == BounceDirection.FLIP_X) {
			   newDirX = -1*this.getBallDirectionX();
			   if(this.getBallDirectionY()<0) {
				   newDirY = this.getBallDirectionY() - 0.1*Math.abs(getBallDirectionX());
			   }else {
				   newDirY = this.getBallDirectionY() + 0.1*Math.abs(getBallDirectionX());
			   }
			   newX = bp.getX() + outgoingDistanceX/this.getBallDirectionX()*newDirX;
			   newY = bp.getY() + outgoingDistanceX/this.getBallDirectionX()*newDirY;
			   // Flipping only Y
		   }else if(bp.getDirection() == BounceDirection.FLIP_Y) {
			   newX = bp.getX();
			   newY = bp.getY();
			   newDirY = -1*this.getBallDirectionY();
			   if(this.getBallDirectionX()<0) {
				   newDirX = this.getBallDirectionX() - 0.1*Math.abs(getBallDirectionY());
			   }else {
				   newDirX = this.getBallDirectionX() + 0.1*Math.abs(getBallDirectionY());
			   }
			   newX = bp.getX() + outgoingDistanceY/this.getBallDirectionY()*newDirX;
			   newY = bp.getY() + outgoingDistanceY/this.getBallDirectionY()*newDirY;
		   }
	   }
	   this.setBallDirectionX(newDirX);
	   this.setBallDirectionY(newDirY);
	   this.setCurrentBallX(newX);
	   this.setCurrentBallY(newY);
   }

  // line 410 "../../../../../Block223States.ump"
   private void doHitBlockNextLevel(){
	   	this.doHitBlock();
	   	int level = this.getCurrentLevel();
	   	this.setCurrentLevel(level+1);
	   	// Change paddle length math
	   	this.setCurrentPaddleLength(this.getGame().getPaddle().getMaxPaddleLength() - (this.getGame().getPaddle().getMaxPaddleLength() - this.getGame().getPaddle().getMinPaddleLength())/(this.getGame().numberOfLevels() - 1)*(this.getCurrentLevel() - 1));
	   	this.setWaitTime(INITIAL_WAIT_TIME * Math.pow(this.getGame().getBall().getBallSpeedIncreaseFactor(),(getCurrentLevel() - 1)));
   System.out.println("Wait time after doHitBlockNextLevel() " + this.getWaitTime()); 
   }


  /**
   * 
   * Feature 89 : doHitPaddleOrWall()
   * @author: Mahdis Asaadi
   * 
   */
  // line 420 "../../../../../Block223States.ump"
   private void doHitNothingAndNotOutOfBounds(){
    double x=this.getCurrentBallX();
    double y=this.getCurrentBallY();
    
    double dx=this.getBallDirectionX();
    double dy=this.getBallDirectionY();
    
    setCurrentBallX(x+dx);
    setCurrentBallY(y+dy);
  }

// line 418 "../../../../../Block223States.ump"
   /*
    * Feature 5: doGameOver()
    * @author: Trevor Tabah
    */
   private void doGameOver(){
	   Block223 block223 = getBlock223();
	   Player p = getPlayer();
	   if (p != null) {
		   Game game = getGame();
		   HallOfFameEntry hof = new HallOfFameEntry(score, playername, p, game, block223);
		   System.out.println(game.getHallOfFameEntry(0));
		   game.setMostRecentEntry(hof);
	   }
	   delete();
  }


  /**
   * Not sure if it works
   */
  // line 440 "../../../../../Block223States.ump"
   public HallOfFameEntry getMostRecentEntry(){
    List<HallOfFameEntry> entryList= game.getHallOfFameEntries();
		int index = entryList.size();
		HallOfFameEntry mostRecent = entryList.get(index-1);
		return mostRecent;
  }
   

  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "score" + ":" + getScore()+ "," +
            "lives" + ":" + getLives()+ "," +
            "currentLevel" + ":" + getCurrentLevel()+ "," +
            "waitTime" + ":" + getWaitTime()+ "," +
            "playername" + ":" + getPlayername()+ "," +
            "ballDirectionX" + ":" + getBallDirectionX()+ "," +
            "ballDirectionY" + ":" + getBallDirectionY()+ "," +
            "currentBallX" + ":" + getCurrentBallX()+ "," +
            "currentBallY" + ":" + getCurrentBallY()+ "," +
            "currentPaddleLength" + ":" + getCurrentPaddleLength()+ "," +
            "currentPaddleX" + ":" + getCurrentPaddleX()+ "," +
            "currentPaddleY" + ":" + getCurrentPaddleY()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "bounce = "+(getBounce()!=null?Integer.toHexString(System.identityHashCode(getBounce())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "block223 = "+(getBlock223()!=null?Integer.toHexString(System.identityHashCode(getBlock223())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 74 "../../../../../Block223Persistence.ump"
  private static final long serialVersionUID = 8597675110221231714L ;

  
}