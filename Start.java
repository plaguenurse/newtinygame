import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.effect.*;
import javafx.stage.Stage;
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.shape.*;
import javafx.scene.input.*;
import javafx.scene.transform.*;
import java.lang.Math;
import javafx.animation.*;
import java.util.ArrayList;

public class Start extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		Group root = new Group();
		int width = 3680;
		int height = 900;
		ArrayList<String> input = new ArrayList<String>();
		ArrayList<Rectangle2D> ground = new ArrayList<Rectangle2D>();
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		
		Canvas canvas = new Canvas(width,height);
		
		//start platforms

		
		ground.add(new Rectangle2D(0,0,40,920));
		ground.add(new Rectangle2D(3640,0,80,920));
		ground.add(new Rectangle2D(40,800,1680,160));
		ground.add(new Rectangle2D(40,640,880,160));
		
		ground.add(new Rectangle2D(2000,800,1640,160));
		ground.add(new Rectangle2D(2600,640,240,240));
		ground.add(new Rectangle2D(3000,440,240,360));
		ground.add(new Rectangle2D(3400,240,240,560));

		//end platforms
		//Start Enemies
		//enemies.add(new NumChucker(3420,480,-1, enemies));
		
		//enemies.add(new BlockHaver(590,820, enemies));
		enemies.add(new Cheesy(960,760, enemies));
		enemies.add(new Cheesy(2360,760, enemies));
		enemies.add(new Cheesy(2400,760, enemies));
		enemies.add(new TinyNums(3240,780, enemies));
		enemies.add(new TinyNums(3280,780, enemies));
		enemies.add(new TinyNums(3320,780, enemies));
		enemies.add(new TinyNums(3360,780, enemies));
		//enemies.add(new Cheesy(640,820, enemies));
		enemies.add(new Bloop(2970,480,enemies));
		//Endemies
		
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
				
		
		root.getChildren().add(canvas);
		
        Scene TheScene = new Scene(root,1024,768,Color.web("#9eb6b7"));
		primaryStage.setScene(TheScene);
		
		TheScene.setOnKeyPressed(
		new EventHandler<KeyEvent>()
		{
			public void handle(KeyEvent e)
			{
				String code = e.getCode().toString();
				if ( !input.contains(code) )
					input.add( code );
			}
		});

		TheScene.setOnKeyReleased(
		new EventHandler<KeyEvent>()
		{
			public void handle(KeyEvent e)
			{
				String code = e.getCode().toString();
				input.remove( code );
			}
		});
		
		
		new PersonAction(gc, input, ground, enemies, width, height).start();
		
		
        primaryStage.show();
        
	}

	
	public static void main (String args[]) 
	{
		launch(args);
	}
}

class MagicPoint
{
	double x;
	double y;
	int type;
	MagicPoint(double x, double y, int type)
	{
		this.x = x;
		this.y = y;
		this.type = type;
	}
	double getX()
	{
		return x;
	}
	double getY()
	{
		return y;
	}
	void render(GraphicsContext gc, Image[] background)
	{
		if(this.type==1)
		{
			gc.drawImage(background[0],this.x,this.y);
		}
		else if(this.type==3)
		{
			gc.drawImage(background[2],this.x,this.y);
		}
		else if(this.type==7)
		{
			gc.drawImage(background[6],this.x,this.y);
		}
		else if(this.type==9)
		{
			gc.drawImage(background[8],this.x,this.y);
		}
		else if(this.type==10)
		{
			gc.drawImage(background[9],this.x,this.y);
		}
		else if(this.type==11)
		{
			gc.drawImage(background[10],this.x,this.y);
		}
		else if(this.type==12)
		{
			gc.drawImage(background[11],this.x,this.y);
		}
		else if(this.type==13)
		{
			gc.drawImage(background[12],this.x,this.y);
		}
		else if(this.type==14)
		{
			gc.drawImage(background[13],this.x,this.y);
		}
		else if(this.type==15)
		{
			gc.drawImage(background[14],this.x,this.y);
		}
		else if(this.type==16)
		{
			gc.drawImage(background[15],this.x,this.y);
		}
		else if(this.type==17)
		{
			gc.drawImage(background[16],this.x,this.y);
		}
	}
	int getType()
	{
		return type;
	}
}

class PersonAction extends AnimationTimer 
{
	int score = 0;
	int scoreMultiplier = 1;
	GraphicsContext gc;
	Person character = new Person(60,520);
	ArrayList<String> input;
	ArrayList<Rectangle2D> ground;
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<GameElement> gameElements = new ArrayList<GameElement>();

	boolean dead = false;
	int countdown = 0;
	
	Image backwall;
	
	Image fullheart = new Image("img/gameelements/fullheart.png");
	Image halfheart = new Image("img/gameelements/halfheart.png");
	
	Image[] background = new Image[18];

	double tick = 0;
	
	long jumptime = 0;
	double jumpheight = 0;
	
	final double MAXJUMPHEIGHT = 190.0;
	
	boolean test = false;
	

	double xacceleration = 0.0;
	double yacceleration = 0.0;
	
	boolean jump = false;
	boolean canjump = true;
	
	boolean floortouch = true;
	int floornum = 0;
	
	boolean walltouch = true;
	int wallnum = 0;
	
	boolean headtouch = true;
	int headnum = 0;
	
	boolean spiketouch = false;
	Rectangle2D spikenum;
	
	int boardWidth = 0;
	int boardHeight = 0;
	
	public PersonAction(GraphicsContext gc,ArrayList<String> input, ArrayList<Rectangle2D> ground, ArrayList<Enemy> enemies,int boardWidth,int boardHeight)
	{
		this.gc = gc;
		this.input = input;
		this.ground = ground;
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
		this.backwall = new Image("img/background.png");
		this.enemies = enemies;
		
		gc.getCanvas().setTranslateY(-1.0 * (character.gety()-384));
		
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		gameElements.add(new Cloud(Math.random()*boardWidth,Math.random()*768,1,boardWidth));
		
		background[0] = new Image("img/ground/Ground1.png");
		background[1] = new Image("img/ground/Ground2.png");
		background[2] = new Image("img/ground/Ground3.png");
		background[3] = new Image("img/ground/Ground4.png");
		background[4] = new Image("img/ground/Ground5.png");
		background[5] = new Image("img/ground/Ground6.png");
		background[6] = new Image("img/ground/Ground7.png");
		background[7] = new Image("img/ground/Ground8.png");
		background[8] = new Image("img/ground/Ground9.png");
		background[9] = new Image("img/ground/Ground10.png");
		background[10] = new Image("img/ground/Ground11.png");
		background[11] = new Image("img/ground/Ground12.png");
		background[12] = new Image("img/ground/Ground13.png");
		background[13] = new Image("img/ground/Ground14.png");
		background[14] = new Image("img/ground/Ground15.png");
		background[15] = new Image("img/ground/Ground16.png");
		background[16] = new Image("img/ground/Ground17.png");
		background[17] = new Image("img/ground/Ground18.png");
	}
	
	@Override
	public void handle(long currentNanoTime)
	{
			if(character.gety()>boardHeight || character.getHealth()<1)
			{
				dead = true;
			}
			
			spiketouch=false;
			gc.clearRect(-10-gc.getCanvas().getTranslateX(),0,boardWidth-gc.getCanvas().getTranslateX()+10,boardHeight);
			
			
			//Scrolling
			if(character.getx()+gc.getCanvas().getTranslateX()>640)
			{
				if (character.getx()+384<boardWidth)
					gc.getCanvas().setTranslateX(-1.0 * (character.getx()-640));
				else
					gc.getCanvas().setTranslateX(-boardWidth+1024);
			}
			else if(character.getx()+gc.getCanvas().getTranslateX()<160)
			{
				if(character.getx()-160>0)
					gc.getCanvas().setTranslateX(-1.0 * (character.getx()-160));
				else
					gc.getCanvas().setTranslateX(0);
			}
			if (character.gety()+384<boardHeight && character.gety()>384)
				gc.getCanvas().setTranslateY(-1.0 * (character.gety()-384));

			//End Scrolling
			//Draw Background + Clouds
			for(int i = 0; i<boardWidth;i+=400)
			{
				gc.drawImage(backwall,i-gc.getCanvas().getTranslateX()/2,408-gc.getCanvas().getTranslateY());
			}
			for(int i = 0;i<gameElements.size();i++)
			{
				if(gameElements.get(i).isPresent()&& gameElements.get(i).isParallax())
				{
					gameElements.get(i).update(tick);
					gc.drawImage(gameElements.get(i).getImage(),gameElements.get(i).getX()-gc.getCanvas().getTranslateX()/gameElements.get(i).getParallaxSpeed(),gameElements.get(i).getY()-gc.getCanvas().getTranslateY());
				}
			}
			//End Background
			//Draw Character
			if((countdown>0 && tick%8<4) || countdown<1)
			{
				gc.drawImage(character.getImage(),character.getx(),character.gety());
				gc.drawImage(character.getSpikeImage(),character.getSpikeX(),character.getSpikeY());
			}
			//End Draw Character
			//Draw Enemies + Checking
			for(int i = 0;i<enemies.size();i++)
			{
				if(enemies.get(i).isPresent())
				{
					if(enemies.get(i).update(ground,character.getBoundingBoxSpike(),tick))
					{
						if(!enemies.get(i).isPresent())
						{
							gameElements.add(new Score(enemies.get(i).getX()-10, enemies.get(i).getY() - 10, 100*scoreMultiplier));
							score+=scoreMultiplier*100;
							scoreMultiplier*=2;
						}
						spiketouch=true;
						spikenum=enemies.get(i).getCollisionBox();
					}
					gc.drawImage(enemies.get(i).getImage(),enemies.get(i).getX(),enemies.get(i).getY());
				}
			}
			//End Enemy Behaviour
			
			
			
			
			
			
			//Ground
			ArrayList<MagicPoint> corners = new ArrayList<MagicPoint>();
			
			for(int i = 0; i<ground.size();i++)
			{
				if(!groundTest('5',ground,ground.get(i)))
				{
					if(ground.get(i).getWidth()==40)
					{
						if(ground.get(i).getHeight()==40)
						{
							gc.drawImage(background[17],ground.get(i).getMinX(),ground.get(i).getMinY());
						}
						else
						{
							for(int j = 0; j< ground.get(i).getHeight();j+=40)
							{
								if(j==0)
									gc.drawImage(background[16],ground.get(i).getMinX(),ground.get(i).getMinY());
								else if (j == ground.get(i).getHeight()-40)
									gc.drawImage(background[15],ground.get(i).getMinX(),ground.get(i).getMinY()+j);
								else
									gc.drawImage(background[4],ground.get(i).getMinX(),ground.get(i).getMinY()+j);
							}
						}
					}
					else if(ground.get(i).getHeight()==40)
					{
						for(int j = 0; j< ground.get(i).getWidth();j+=40)
						{
							if(j==0)
								gc.drawImage(background[13],ground.get(i).getMinX(),ground.get(i).getMinY());
							else if (j == ground.get(i).getWidth()-40)
								gc.drawImage(background[14],ground.get(i).getMinX()+j,ground.get(i).getMinY());
							else
								gc.drawImage(background[7],ground.get(i).getMinX()+j,ground.get(i).getMinY());
						}
					}
					else
					{
						for(int j = 0; j< ground.get(i).getWidth();j+=40)
						{
							for(int k = 0; k< ground.get(i).getHeight();k+=40)
							{
								if(j==0 && k==0)
									gc.drawImage(background[0],ground.get(i).getMinX(),ground.get(i).getMinY());
								
								else if (j == ground.get(i).getWidth()-40 && k == ground.get(i).getHeight()-40 )
									gc.drawImage(background[8],ground.get(i).getMinX()+j,ground.get(i).getMinY()+k);
								
								else if (j == 0 && k == ground.get(i).getHeight()-40 )
									gc.drawImage(background[6],ground.get(i).getMinX()+j,ground.get(i).getMinY()+k);
								
								else if (j == ground.get(i).getWidth()-40 && k == 0)
									gc.drawImage(background[2],ground.get(i).getMinX()+j,ground.get(i).getMinY()+k);
								
								else if (k == ground.get(i).getHeight()-40 )
									gc.drawImage(background[7],ground.get(i).getMinX()+j,ground.get(i).getMinY()+k);
								
								else
									gc.drawImage(background[5],ground.get(i).getMinX()+j,ground.get(i).getMinY()+k);
							}
						}
					}
				}
				else//collidey platforms
				{
					if(ground.get(i).getHeight()>40)
					{
						if(ground.get(i).getWidth()>40)
						{
							if(!groundTest('1',ground,ground.get(i)))
							{
								corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),1));
							}
							else
							{
								gc.drawImage(background[4],ground.get(i).getMinX(),ground.get(i).getMinY());

							}
							if(!groundTest('3',ground,ground.get(i)))
							{
								corners.add(new MagicPoint(ground.get(i).getMaxX()-40,ground.get(i).getMinY(),3));
							}
							else
							{
								gc.drawImage(background[4],ground.get(i).getMaxX()-40,ground.get(i).getMinY());

							}
							if(!groundTest('7',ground,ground.get(i)))
							{
								corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMaxY()-40,7));
							}
							else
							{
								gc.drawImage(background[4],ground.get(i).getMinX(),ground.get(i).getMaxY()-40);

							}
							if(!groundTest('9',ground,ground.get(i)))
							{
								corners.add(new MagicPoint(ground.get(i).getMaxX()-40,ground.get(i).getMaxY()-40,9));
							}
							else
							{
								gc.drawImage(background[4],ground.get(i).getMaxX()-40,ground.get(i).getMaxY()-40);

							}
						}
						else
						{
							if(!groundTest('U',ground,ground.get(i)))
							{
								if(groundTest('3',ground,ground.get(i)) && !groundTest('1',ground,ground.get(i)))
									corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),1));
								else if(!groundTest('3',ground,ground.get(i)) && groundTest('1',ground,ground.get(i)))
									corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),3));
								else
									corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),17));
							}
							if(!groundTest('D',ground,ground.get(i)))
							{
								if(groundTest('9',ground,ground.get(i)) && !groundTest('7',ground,ground.get(i)))
									corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMaxY()-40,7));
								else if(!groundTest('9',ground,ground.get(i)) && groundTest('7',ground,ground.get(i)))
									corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMaxY()-40,9));
								else
									corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMaxY()-40,16));
							}
						}
					}
					else if(ground.get(i).getWidth()>40)
					{
						if(!groundTest('L',ground,ground.get(i)))
						{
							if(groundTest('1',ground,ground.get(i)) && !groundTest('7',ground,ground.get(i)))
								corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),7));
							else if(groundTest('7',ground,ground.get(i)) && !groundTest('1',ground,ground.get(i)))
								corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),1));
							else
								corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),14));
						}
						if(!groundTest('R',ground,ground.get(i)))
						{
							if(groundTest('3',ground,ground.get(i)) && !groundTest('9',ground,ground.get(i)))
								corners.add(new MagicPoint(ground.get(i).getMaxX()-40,ground.get(i).getMinY(),9));
							else if(!groundTest('3',ground,ground.get(i)) && groundTest('9',ground,ground.get(i)))
								corners.add(new MagicPoint(ground.get(i).getMaxX()-40,ground.get(i).getMinY(),3));
							else
								corners.add(new MagicPoint(ground.get(i).getMaxX()-40,ground.get(i).getMinY(),15));
						}
					}
					else
					{
						if(groundTest('L',ground,ground.get(i)) && groundTest('U',ground,ground.get(i)) && !groundTest('R',ground,ground.get(i)) && !groundTest('D',ground,ground.get(i)))
						{
							corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),9));
						}
						else if(groundTest('R',ground,ground.get(i)) && groundTest('U',ground,ground.get(i)) && !groundTest('L',ground,ground.get(i)) && !groundTest('D',ground,ground.get(i)))
						{
							corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),7));
						}
						else if(groundTest('L',ground,ground.get(i)) && groundTest('D',ground,ground.get(i)) && !groundTest('R',ground,ground.get(i)) && !groundTest('U',ground,ground.get(i)))
						{
							corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),3));
						}
						else if(groundTest('R',ground,ground.get(i)) && groundTest('D',ground,ground.get(i)) && !groundTest('L',ground,ground.get(i)) && !groundTest('U',ground,ground.get(i)))
						{
							corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),1));
						}
						else if(!groundTest('U',ground,ground.get(i)) && groundTest('D',ground,ground.get(i)))
						{
							corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),17));
						}
						else if(!groundTest('D',ground,ground.get(i)) && groundTest('U',ground,ground.get(i)))
						{
							corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),16));
						}
						else if(!groundTest('L',ground,ground.get(i)) && groundTest('R',ground,ground.get(i)))
						{
							corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),14));
						}
						else if(!groundTest('R',ground,ground.get(i)) && groundTest('L',ground,ground.get(i)))
						{
							corners.add(new MagicPoint(ground.get(i).getMinX(),ground.get(i).getMinY(),15));
						}
						else
							gc.drawImage(background[4],ground.get(i).getMinX(),ground.get(i).getMinY());
					}
					for(double j = ground.get(i).getMinY();j<=ground.get(i).getMaxY();j+=40)
					{
						for(int k = 0; k<ground.size();k++)
						{
							if(k!=i && ground.get(k).getMinX() != ground.get(i).getMinX() && ground.get(k).contains(ground.get(i).getMinX(),j))
							{
								if(ground.get(k).getMinY()==j && j!=ground.get(i).getMinY())
								{
									corners.add(new MagicPoint(ground.get(i).getMinX()-40,j-40,11));
								}
								else if(ground.get(k).getMaxY()==j && j!= ground.get(i).getMaxY())
								{
									corners.add(new MagicPoint(ground.get(i).getMinX()-40,j,13));
								}
							}
							if(k!=i && ground.get(k).getMaxX() != ground.get(i).getMaxX() && ground.get(k).contains(ground.get(i).getMaxX(),j))
							{
								if(ground.get(k).getMinY()==j && j!=ground.get(i).getMinY())
								{
									corners.add(new MagicPoint(ground.get(i).getMaxX(),j-40,10));
								}
								else if(ground.get(k).getMaxY()==j && j!= ground.get(i).getMaxY())
								{
									corners.add(new MagicPoint(ground.get(i).getMaxX(),j,12));
								}
							}
						}
					}
			
					for (double gwidth = ground.get(i).getMinX(); gwidth<ground.get(i).getMaxX();gwidth+=40)
					{
						for (double gheight = ground.get(i).getMinY(); gheight<ground.get(i).getMaxY();gheight+=40)
						{
							boolean testMagic = false;
							for(int l = 0; l<corners.size();l++)
							{
								if (corners.get(l).getX()==gwidth && corners.get(l).getY()==gheight)
								{
									testMagic = true;
								}
							}
							if(ground.get(i).getWidth()>40 && gheight==ground.get(i).getMaxY()-40)
							{
								boolean floorTest = false;
								for(int l = 0;l<ground.size();l++)
								{
									if(ground.get(l).contains(gwidth+1, gheight+42) )
										floorTest = true;
								}
								if(!testMagic)
								{
										if(!floorTest)
											gc.drawImage(background[7],gwidth,gheight);
										else
											gc.drawImage(background[4],gwidth,gheight);
								}
							}
							else if(!testMagic)
							{
									gc.drawImage(background[4],gwidth,gheight);
							}
						}
					}
				}
			}
			
			for(int i = 0;i<corners.size();i++)
			{
				corners.get(i).render(gc,background);
			}
			//End Ground
			//Game Elements
			for(int i = 0;i<gameElements.size();i++)
			{
				if(gameElements.get(i).isPresent()&& !gameElements.get(i).isParallax())
				{
					gameElements.get(i).update(tick);
					gc.drawImage(gameElements.get(i).getImage(),gameElements.get(i).getX(),gameElements.get(i).getY());
				}
			}
			//End Game Elements
			//Overlay
			for(int i = 0;i<character.getHealth();i++)
			{
				if(character.getHealth()-i>1)
				{
					gc.drawImage(fullheart,23+23*(i/2)-gc.getCanvas().getTranslateX(),23-gc.getCanvas().getTranslateY());
					i++;
				}
				else
				{
					gc.drawImage(halfheart,23+23*(i/2)-gc.getCanvas().getTranslateX(),23-gc.getCanvas().getTranslateY());
				}
			}
			//End Overlay
			//Enemy Attacks
			if(countdown==0)
			{
				for(int i = 0; i< enemies.size();i++)
				{
					if(character.getBoundingBox().intersects(enemies.get(i).getCollisionBox()) && enemies.get(i).getHurty())
					{
						character.healthHit();
						countdown = 60;
					}
				}
			}
			//End Enemy Attacks
			//Keyboard input
			if (input.contains("D"))
			{
				if (character.getdir()<1)
					character.setDirAndRender(1);
				character.walk(tick);
				if(xacceleration<10)
					xacceleration++;
			}
			if (input.contains("A"))
			{
				if (character.getdir()>-1)
					character.setDirAndRender(-1);
				character.walk(tick);
				if(xacceleration>-10)
					xacceleration--;				
			}
			
			if (input.contains("UP") && !input.contains("LEFT") && !input.contains("RIGHT"))
			{
				test = false;
				for(int i = 0; i<ground.size() && !test;i++)
				{
					if(character.getBoundingBoxSpikeDir(8).intersects(ground.get(i)) ||
					ground.get(i).contains(character.getBoundingBoxSpikeDir(8)))
					{
						test = true;	
					}
				}
				if(!test)
				{
					character.setSpikeDir(8);
				}
			}
			else if (input.contains("RIGHT")&& !input.contains("UP") && !input.contains("DOWN"))
			{
				test= false;
				for(int i = 0; i<ground.size() && !test;i++)
				{
					if(character.getBoundingBoxSpikeDir(6).intersects(ground.get(i)) ||
					ground.get(i).contains(character.getBoundingBoxSpikeDir(6)))
						test = true;	
				}
				if(!test)
				{
					character.setSpikeDir(6);
				}

			}
			else if (input.contains("LEFT")&& !input.contains("UP") && !input.contains("DOWN"))
			{
				test= false;
				for(int i = 0; i<ground.size() && !test;i++)
				{
					if(character.getBoundingBoxSpikeDir(4).intersects(ground.get(i)))
						test = true;	
				}
				if(!test)
				{
					character.setSpikeDir(4);
				}
			}
			else if (input.contains("DOWN")&& !input.contains("LEFT") && !input.contains("RIGHT"))
			{	
				test= false;
				for(int i = 0; i<ground.size() && !test;i++)
				{
					if(character.getBoundingBoxSpikeDir(2).intersects(ground.get(i)))
						test = true;	
				}
				if(!test)
				{
					character.setSpikeDir(2);
				}
			}
			else if (input.contains("UP") && input.contains("RIGHT"))
			{
				test= false;
				for(int i = 0; i<ground.size() && !test;i++)
				{
					if(character.getBoundingBoxSpikeDir(9).intersects(ground.get(i)))
						test = true;	
				}
				if(!test)
				{
					character.setSpikeDir(9);
				}
			}
			else if (input.contains("UP") && input.contains("LEFT"))
			{
				test= false;
				for(int i = 0; i<ground.size() && !test;i++)
				{
					if(character.getBoundingBoxSpikeDir(7).intersects(ground.get(i)))
						test = true;	
				}
				if(!test)
				{
					character.setSpikeDir(7);
				}
			}
			else if (input.contains("DOWN") && input.contains("LEFT"))
			{
				test= false;
				for(int i = 0; i<ground.size() && !test;i++)
				{
					if(character.getBoundingBoxSpikeDir(1).intersects(ground.get(i)))
						test = true;	
				}
				if(!test)
				{
					character.setSpikeDir(1);
				}
			}
			else if (input.contains("DOWN") && input.contains("RIGHT"))
			{
				test= false;
				for(int i = 0; i<ground.size() && !test;i++)
				{
					if(character.getBoundingBoxSpikeDir(3).intersects(ground.get(i)))
						test = true;	
				}
				if(test== false)
				{
					character.setSpikeDir(3);
				}
			}
			if (input.contains("SPACE") && canjump)
			{
				jump=true;
				jumptime=(int)tick;
				jumpheight=character.gety();
			}
			//End Keyboard Input
			//Jumping
			if (jump)
			{
				character.sety(jumpheight - MAXJUMPHEIGHT + Math.pow(((tick - jumptime)/2.0 - Math.sqrt(MAXJUMPHEIGHT)),2)  );
			}
			//End Jumping
			//Floor, Wall & Spike Collision
			//Head
			headtouch=false;
			for(headnum = 0; headnum<ground.size() && !headtouch;headnum++)
			{
				headtouch= false;
				if(character.getBoundingBoxHead().intersects(ground.get(headnum)))
					headtouch= true;	
			}
			//Floor
			floortouch=false;
			for(floornum = 0; floornum<ground.size() && !floortouch;floornum++)
			{
				floortouch= false;
				if(character.getBoundingBoxFloor().intersects(ground.get(floornum)))
					floortouch= true;	
			}
			//Wall
			walltouch=false;
			for(wallnum = 0; wallnum<ground.size() && !walltouch;wallnum++)
			{
				walltouch= false;
				if(character.getBoundingBoxWall().intersects(ground.get(wallnum)))
					walltouch= true;	
			}
			//Spike
			for(int i = 0; i<ground.size() && !spiketouch;i++)
			{
				spiketouch= false;
				if(character.getBoundingBoxSpike().intersects(ground.get(i)))
					spiketouch= true;
					spikenum = ground.get(i);	
			}
			//
			//Spike Physics
			if(spiketouch)
			{
				
				if(character.getSpikeDir() == 1 || character.getSpikeDir() == 4 || character.getSpikeDir() == 7)
				{
					if(xacceleration<0 && spikenum.getMinY() < character.getSpikeY())
					{
						xacceleration = -xacceleration*2.6;
					}
				}
				if(character.getSpikeDir() == 3 || character.getSpikeDir() == 6 || character.getSpikeDir() == 9)
				{
					if(xacceleration>0 && spikenum.getMinY() < character.getSpikeY())
					{
						xacceleration = -xacceleration*2.6;
					}
				}
				if(character.getSpikeDir() == 2 || character.getSpikeDir() == 1 || character.getSpikeDir() == 3)
				{
					if(yacceleration>0)
					{
						yacceleration = -yacceleration*1;
					}
					if(jump)
					{
						jump=false;
						yacceleration= -Math.abs(((tick - jumptime)/2.0 - Math.sqrt(MAXJUMPHEIGHT)))*.9;
					}
				}
				if(character.getSpikeDir() > 6)
				{
					if(yacceleration<0)
					{
						yacceleration = -yacceleration*1;
					}
					if(jump)
					{
						jump=false;
						yacceleration= -Math.abs(((tick - jumptime)/2.0 - Math.sqrt(MAXJUMPHEIGHT)))*.9;
					}
				}
				
			}
			//End Spike Physics
			//Floor & Wall Physics
			if(floortouch)
			{
				scoreMultiplier=1;
				if(ground.get(floornum-1).getMinY()>character.gety())
				{
					yacceleration=0;
					if(!canjump) jump=false;
					canjump=true;
					character.sety(ground.get(floornum-1).getMinY()-40);
				}
			}
			else
			{
				canjump=false;
				character.sety(character.gety()+yacceleration);
				yacceleration+=.3;
			}
			if(headtouch)
			{
				jump=false;
				if(yacceleration<0)
					yacceleration=.3;
				if(ground.get(headnum-1).getMaxY()>character.gety())
				{
					character.sety(ground.get(headnum-1).getMaxY()+.3);
				}			
			}
			
			if(walltouch)
			{
				if(ground.get(wallnum-1).getMinX()>character.getx())
				{
					if(character.getx()+character.getwidth()> ground.get(wallnum-1).getMinX())
						character.setx(ground.get(wallnum-1).getMinX()-character.getwidth());
					if (xacceleration>0) 
						xacceleration=-xacceleration/3.0;
				}
				if(ground.get(wallnum-1).getMaxX()<character.getx()+40)
				{
					if(character.getx()< ground.get(wallnum-1).getMaxX())
						character.setx(ground.get(wallnum-1).getMaxX());
					if (xacceleration<0) 
						xacceleration=-xacceleration/3.0;
				}
			}
			//End Floor & Wall Physics
			//Horizontal acceleration
			character.setx(character.getx()+xacceleration);
			if(floortouch)
				xacceleration=xacceleration*.9;
			else
				xacceleration=xacceleration*.97;
			//End Horizontal acceleration
			tick++;
			if(countdown>0)
				countdown--;
			if(dead)
			{
				gc.getCanvas().setTranslateX(0);
				gc.getCanvas().setTranslateY(0);
				gc.drawImage(new Image("img/death.png"),0,0);
				this.stop();
			}
			
	}
	private boolean groundTest(char direction, ArrayList<Rectangle2D> ground,Rectangle2D testPoint)
	{
		if(direction == '1')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && (ground.get(i).contains(testPoint.getMinX()-3,testPoint.getMinY()+3)||ground.get(i).contains(testPoint.getMinX()+3,testPoint.getMinY()-3)))
					return true;
			}
		}
		else if(direction == '2')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && ground.get(i).contains(new Rectangle2D(testPoint.getMinX()-2,testPoint.getMaxY()+2,testPoint.getWidth()+4,2)) )
					return true;
			}
		}
		else if(direction == '3')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && (ground.get(i).contains(testPoint.getMaxX()+3,testPoint.getMinY()+3)||ground.get(i).contains(testPoint.getMaxX()-3,testPoint.getMinY()-3)))
					return true;
			}
		}
		else if(direction == '4')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && ground.get(i).contains(new Rectangle2D(testPoint.getMinX()-2,testPoint.getMinY()-2,2,testPoint.getHeight()+4)) )
					return true;
			}
		}
		else if(direction == '5')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && 
					(ground.get(i).intersects(new Rectangle2D(testPoint.getMinX()+2,testPoint.getMinY()-2,testPoint.getWidth()-4,testPoint.getHeight()+4)) 
						|| ground.get(i).intersects(new Rectangle2D(testPoint.getMinX()-2,testPoint.getMinY()+2,testPoint.getWidth()+4,testPoint.getHeight()-4))))
					return true;
			}
		}
		else if(direction == '7')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && (ground.get(i).contains(testPoint.getMinX()-3,testPoint.getMaxY()-3)||ground.get(i).contains(testPoint.getMinX()+3,testPoint.getMaxY()+3)))
					return true;
			}
		}
		else if(direction == '8')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && ground.get(i).contains(new Rectangle2D(testPoint.getMinX()-2,testPoint.getMinY()-2,testPoint.getWidth()+4,2)) )
					return true;
			}
		}
		else if(direction == '9')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && (ground.get(i).contains(testPoint.getMaxX()+3,testPoint.getMaxY()-3)||ground.get(i).contains(testPoint.getMaxX()-3,testPoint.getMaxY()+3)))
					return true;
			}
		}
		else if(direction == 'U')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && ground.get(i).contains(new Rectangle2D(testPoint.getMinX()+2,testPoint.getMinY()-2,testPoint.getWidth()-2,2)) )
					return true;
			}
		}
		else if(direction == 'D')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && ground.get(i).contains(new Rectangle2D(testPoint.getMinX()+2,testPoint.getMaxY()+2,testPoint.getWidth()-2,2)) )
					return true;
			}
		}
		else if(direction == 'L')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && ground.get(i).contains(new Rectangle2D(testPoint.getMinX()-2,testPoint.getMinY()+2,2,testPoint.getHeight()-4)) )
					return true;
			}
		}
		else if(direction == 'R')
		{
			for(int i = 0;i<ground.size();i++)
			{
				if(!ground.get(i).equals(testPoint) && ground.get(i).contains(new Rectangle2D(testPoint.getMaxX()+2,testPoint.getMinY()+2,2,testPoint.getHeight()-4)) )
					return true;
			}
		}
		
		return false;
	}
}

abstract class GameElement
{
	int width;
	int height;
	short cycle = 0;
	boolean parallax = false;
	double parallaxSpeed;
	boolean present = true;
	double x;
	double y;
	Image face;
	boolean front = true;
	abstract void update(double tick);
	Image getImage()
	{
		return face;
	}
	Rectangle2D getCollisionBox()
	{
		return new Rectangle2D(x,y,width,height);
	}
	boolean isPresent()
	{
		return present;
	}
	boolean isParallax()
	{
		return parallax;
	}
	double getX()
	{
		return x;
	}
	double getParallaxSpeed()
	{
		return parallaxSpeed;
	}
	double getY()
	{
		return y;
	}
	int getWidth()
	{
		return width;
	}
	int getHeight()
	{
		return height;
	}
}

class Cloud extends GameElement
{
	int direction;
	double speed;
	int maxWidth;
	Image[] faceset;
	Cloud(double x, double y, int direction, int maxWidth)
	{
		this.x=x;
		this.y=y;
		this.direction=direction;
		front=false;
		this.maxWidth=maxWidth;
		this.parallax=true;
		this.parallaxSpeed=3;
		this.speed=(Math.random()*3+1)/3;
		faceset = new Image[3];
		this.faceset[0] = new Image("img/cloud/cloud1.png");
		this.faceset[1] = new Image("img/cloud/cloud2.png");
		this.faceset[2] = new Image("img/cloud/cloud3.png");
		this.face=faceset[(int)Math.floor(Math.random()*3)];
		this.width=(int)face.getWidth();
		this.height=(int)face.getHeight();
	}
	void update(double tick)
	{
		this.x+=speed*direction;
		if(this.x>maxWidth && direction==1)
			this.x=-this.width;
	}
}

class Score extends GameElement
{
	Image score100 = new Image("img/gameelements/100.png");
	Image score200 = new Image("img/gameelements/200.png");
	Image score400 = new Image("img/gameelements/400.png");
	Image score800 = new Image("img/gameelements/800.png");
	int fade = 0;
	Score(double x, double y, int score)
	{
		this.x=x;
		this.y=y;
		if(score<400)
		{
			this.height = 20;
			this.width = 40;

		}
		else
		{
			this.height = 30;
			this.width = 50;

		}
		parallax = false;
		if(score==100)
		{
			this.face = score100;
		}
		else if(score==200)
		{
			this.face = score200;
		}
		else if(score==400)
		{
			this.face = score400;
		}
		else if(score==800)
		{
			this.face = score800;
		}
	}
	void update(double tick)
	{
		fade++;
		if(tick%8==0)
		{
			this.y-=3;
		}
		if (fade== 48)
		{
			present=false;
		}
	}
}

abstract class Enemy
{
	int enemytype;
	int width;
	int height;
	int direction;
	short cycle = 0;
	boolean hit = false;
	boolean hurty = true;
	boolean present = true;
	double x;
	double y;
	Image[] faceset;
	Image face;
	abstract boolean update(ArrayList<Rectangle2D> ground, Rectangle2D spike, double tick);
	Image getImage()
	{
		return face;
	}
	Rectangle2D getCollisionBox()
	{
		return new Rectangle2D(x,y,width,height);
	}
	boolean isPresent()
	{
		return present;
	}
	boolean getHurty()
	{
		return hurty;
	}
	double getX()
	{
		return x;
	}
	double getY()
	{
		return y;
	}
	int getWidth()
	{
		return width;
	}
	int getHeight()
	{
		return height;
	}	
}

class NumChucker extends Enemy
{
	ArrayList<Enemy> enemyList;
	int health = 4;
	NumChucker(double x, double y, int direction, ArrayList<Enemy> enemyList)
	{
		this.width = 80;
		this.height = 80;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.enemyList = enemyList;
		faceset = new Image[14];
		this.faceset[0] = new Image("img/numchucker/numchucker1.png");
		this.faceset[1] = new Image("img/numchucker/numchucker2.png");
		this.faceset[2] = new Image("img/numchucker/numchucker3.png");
		this.faceset[3] = new Image("img/numchucker/numchucker4.png");
		this.faceset[4] = new Image("img/numchucker/numchucker5.png");
		this.faceset[5] = new Image("img/numchucker/numchucker6.png");
		this.faceset[6] = new Image("img/numchucker/numchucker7.png");
		this.faceset[7] = new Image("img/numchucker/numchucker8.png");
		this.faceset[8] = new Image("img/numchucker/numchucker9.png");
		this.faceset[9] = new Image("img/numchucker/numchucker10.png");
		this.faceset[10] = new Image("img/numchucker/numchucker11.png");
		this.faceset[11] = new Image("img/numchucker/numchucker12.png");
		this.faceset[12] = new Image("img/numchucker/numchucker13.png");
		this.faceset[13] = new Image("img/numchucker/numchucker14.png");
		
		this.face= faceset[0];
	}
	boolean update(ArrayList<Rectangle2D> ground, Rectangle2D spike, double tick)
	{
		hit=false;
		if(this.getCollisionBox().intersects(spike))
		{
			hit=true;
			health--;
			if(health==0)
			{
				present=false;
				hurty=false;
			}
		}
		if(tick%95==12)
		{
			enemyList.add(new Numjectile(this.x+30, this.y+20,enemyList,direction,tick));
		}
		if(tick%95<24)
		{
			if(tick%7==0)
			{ 
				if(direction==-1)
				{
					if(cycle>6|| cycle<4)
						cycle=4;
				}
				else
				{
					if(cycle>13|| cycle<11)
						cycle=11;
				}
				face=faceset[cycle];
				cycle++;
			}
		}
		else
		{
if(tick%7==0)
			{ 
				if(direction==-1)
				{
					if(cycle>3)
						cycle=0;
				}
				else
				{
					if(cycle>10|| cycle<7)
						cycle=7;
				}
				face=faceset[cycle];
				cycle++;
			}
		}
		return hit;
	}
}

class Numjectile extends Enemy
{
	double startTick;
	double startHeight;
	ArrayList<Enemy> enemyList;

	Numjectile(double x, double y, ArrayList<Enemy> enemyList, int direction, double tick)
	{
		this.width = 20;
		this.height = 20;
		this.startHeight = y;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.startTick = tick;
		this.enemyList = enemyList;
		faceset = new Image[1];
		this.faceset[0] = new Image("img/tinynums/tinynumjectile.png");
	
		this.face= faceset[0];
	}
	boolean update(ArrayList<Rectangle2D> ground, Rectangle2D spike, double tick)
	{
		if(this.getCollisionBox().intersects(spike))
		{
			present=false;
			hurty=false;
			hit=true;
		}
		
		this.x+=3*direction;
		this.y=(startHeight - 75 + Math.pow(((tick - startTick)/2.0 - Math.sqrt(75)),2)  );
		for(int i = 0;i< ground.size();i++)
		{
			if(this.getCollisionBox().intersects(ground.get(i)))
			{
					present=false;
					hurty=false;
					enemyList.add(new TinyNums(this.x,ground.get(i).getMinY()-20,enemyList));
			}
		}
		return hit;
	}
}

class BlockHaver extends Enemy
{
	boolean move = false;
	boolean placing = false;
	int placeTimer = 0;
	ArrayList<Enemy> enemyList;
	BlockHaver(double x, double y, ArrayList<Enemy> enemyList)
	{
		this.width = 40;
		this.height = 40;
		this.x = x;
		this.y = y;
		this.direction = 1;
		this.enemyList = enemyList;
		faceset = new Image[1];
		this.faceset[0] = new Image("img/blockhaver/blockhaver1.png");
		
		this.face= faceset[0];
	}
	boolean update(ArrayList<Rectangle2D> ground, Rectangle2D spike, double tick)
	{
		if(this.getCollisionBox().intersects(spike))
		{
			present=false;
			hurty=false;
			hit=true;
		}
		move = false;
		if(direction==1)
		{
			if(cycle>4)
				cycle = 0;
		}
		else 
		{
			if(cycle < 5 || cycle > 9)
				cycle = 5;
		}
		if(tick%7==0)
		{
			//face=faceset[cycle];
			cycle++;
		}
		
		if(placing)
		{
			if(placeTimer==10)
			{
				if (direction== 1)
					ground.add(new Rectangle2D(this.x+42,this.y,40,40));
				else
					ground.add(new Rectangle2D(this.x-42,this.y,40,40));

			}
			placeTimer--;
			if(placeTimer==0)
			{
				placing= false;
				direction=-direction;
			}
		}
		else
		{
			for(int i = 0;i< ground.size() && !move;i++)
			{
				if(ground.get(i).contains(this.x+42,this.y+38) && direction == 1)
				{
					i=ground.size();
					move = false;
				}
				else if(ground.get(i).contains(this.x-2,this.y+38) && direction == -1)
				{
					i=ground.size();
					move = false;
				}
				else if(ground.get(i).contains(this.x+40,this.y+42) && direction == 1)
				{
					this.x+=1;
					move=true;
				}
				else if(ground.get(i).contains(this.x,this.y+42) && direction == -1)
				{
					this.x-=1;
					move=true;
				}
			}
			for(int i = 0;i< enemyList.size() && move;i++)
			{
				if(enemyList.get(i).getCollisionBox().contains(this.x+42,this.y+38) && direction == 1 && enemyList.get(i).isPresent())
				{
					move = false;
				}
				else if(enemyList.get(i).getCollisionBox().contains(this.x-2,this.y+38) && direction == -1 && enemyList.get(i).isPresent())
				{
					move = false;
				}
			}
			if(!move)
			{
				direction = -direction;
			}
			for(int i = 0;i< ground.size() && move;i++)
			{
				if(ground.get(i).contains(this.x+82,this.y+38) && direction == 1)
				{
					i=ground.size();
					placing = true;
				}
				else if(ground.get(i).contains(this.x-42,this.y+38) && direction == -1)
				{
					i=ground.size();
					placing = true;
				}
			}
			/*for(int i = 0;i< enemyList.size() && placing;i++)
			{
				if(enemyList.get(i).getCollisionBox().intersects(new Rectangle2D(this.x,this.y,80,40)) && direction == 1 && enemyList.get(i).isPresent())
				{
					placing = false;
				}
				else if(enemyList.get(i).getCollisionBox().intersects(new Rectangle2D(this.x,this.y-40,80,40)) && direction == -1 && enemyList.get(i).isPresent())
				{
					placing = false;
				}
			}*/
			if(placing)
				placeTimer=20;
		}
		return hit;
	}
}

class Cheesy extends Enemy
{
	boolean move = false;
	ArrayList<Enemy> enemyList;
	Cheesy(double x, double y, ArrayList<Enemy> enemyList)
	{
		this.width = 40;
		this.height = 40;
		this.x = x;
		this.y = y;
		this.direction = 1;
		this.enemyList = enemyList;
		faceset = new Image[10];
		this.faceset[0] = new Image("img/mrcheesy/MrCheesy1.png");
		this.faceset[1] = new Image("img/mrcheesy/MrCheesy2.png");
		this.faceset[2] = new Image("img/mrcheesy/MrCheesy3.png");
		this.faceset[3] = new Image("img/mrcheesy/MrCheesy4.png");
		this.faceset[4] = new Image("img/mrcheesy/MrCheesy5.png");
		this.faceset[5] = new Image("img/mrcheesy/MrCheesy6.png");
		this.faceset[6] = new Image("img/mrcheesy/MrCheesy7.png");
		this.faceset[7] = new Image("img/mrcheesy/MrCheesy8.png");
		this.faceset[8] = new Image("img/mrcheesy/MrCheesy9.png");
		this.faceset[9] = new Image("img/mrcheesy/MrCheesy10.png");
		this.face= faceset[0];
	}
	boolean update(ArrayList<Rectangle2D> ground, Rectangle2D spike, double tick)
	{
		if(this.getCollisionBox().intersects(spike))
		{
			present=false;
			hurty=false;
			hit=true;
		}
		move = false;
		if(direction==1)
		{
			if(cycle>4)
				cycle = 0;
		}
		else 
		{
			if(cycle < 5 || cycle > 9)
				cycle = 5;
		}
		if(tick%7==0)
		{
			face=faceset[cycle];
			cycle++;
		}
		
		for(int i = 0;i< ground.size();i++)
		{
			if(ground.get(i).contains(this.x+42,this.y+38) && direction == 1)
			{
				move = false;
				i=ground.size();
			}
			else if(ground.get(i).contains(this.x-2,this.y+38) && direction == -1)
			{
				move = false;
				i=ground.size();
			}
			else if(ground.get(i).contains(this.x+40,this.y+42) && direction == 1)
			{
				move=true;
			}
			else if(ground.get(i).contains(this.x,this.y+42) && direction == -1)
			{
				move=true;
			}
		}
		for(int i = 0;i< enemyList.size() && move;i++)
		{
			if(enemyList.get(i).getCollisionBox().contains(this.x+42,this.y+38) && direction == 1 && enemyList.get(i).isPresent())
			{
				move = false;
			}
			else if(enemyList.get(i).getCollisionBox().contains(this.x-2,this.y+38) && direction == -1 && enemyList.get(i).isPresent())
			{
				move = false;
			}
		}
		if(!move)
		{
			direction = -direction;
		}
		else
		{
			this.x+=direction;
		}
		return hit;
	}
}
class TinyNums extends Enemy
{
	boolean move = false;
	ArrayList<Enemy> enemyList;
	TinyNums(double x, double y, ArrayList<Enemy> enemyList)
	{
		this.width = 20;
		this.height = 20;
		this.x = x;
		this.y = y;
		this.direction = 1;
		this.enemyList = enemyList;
		faceset = new Image[8];
		this.faceset[0] = new Image("img/tinynums/tinynums1.png");
		this.faceset[1] = new Image("img/tinynums/tinynums2.png");
		this.faceset[2] = new Image("img/tinynums/tinynums3.png");
		this.faceset[3] = new Image("img/tinynums/tinynums4.png");
		this.faceset[4] = new Image("img/tinynums/tinynums5.png");
		this.faceset[5] = new Image("img/tinynums/tinynums6.png");
		this.faceset[6] = new Image("img/tinynums/tinynums7.png");
		this.faceset[7] = new Image("img/tinynums/tinynums8.png");

		this.face= faceset[0];
	}
	boolean update(ArrayList<Rectangle2D> ground, Rectangle2D spike, double tick)
	{
		if(this.getCollisionBox().intersects(spike))
		{
			present=false;
			hurty=false;
			hit=true;
		}
		move = false;
		if(direction==1)
		{
			if(cycle>3)
				cycle = 0;
		}
		else 
		{
			if(cycle < 4 || cycle > 7)
				cycle = 4;
		}
		if(tick%7==0)
		{
			face=faceset[cycle];
			cycle++;
		}
		
		for(int i = 0;i< ground.size();i++)
		{
			if(ground.get(i).contains(this.x+22,this.y+18) && direction == 1)
			{
				move=false;
				i=ground.size();
			}
			else if(ground.get(i).contains(this.x-2,this.y+18) && direction == -1)
			{
				move = false;
				i=ground.size();
			}
			else if(ground.get(i).contains(this.x+20,this.y+22) && direction == 1)
			{
				move=true;
			}
			else if(ground.get(i).contains(this.x,this.y+22) && direction == -1)
			{
				move=true;
			}
		}
		for(int i = 0;i< enemyList.size() && move;i++)
		{
			if(enemyList.get(i).getCollisionBox().contains(this.x+22,this.y+18) && direction == 1 && enemyList.get(i).isPresent())
			{
				move = false;
			}
			else if(enemyList.get(i).getCollisionBox().contains(this.x-2,this.y+18) && direction == -1 && enemyList.get(i).isPresent())
			{
				move = false;
			}
		}
		if(!move)
		{
			direction = -direction;
		}
		else
		{
			this.x+=direction;
		}
		return hit;
	}
}


class Projectile extends Enemy
{
	double angle;
	double speed;
	Projectile(double x,double y,double angle,double speed)
	{
		this.x=x;
		this.y=y;
		this.angle=angle;
		this.speed=speed;
		this.width=10;
		this.height=10;
		this.faceset = new Image[1];
		this.faceset[0] = new Image("img/bullet.png");
		this.face = faceset[0];
	}
	boolean update(ArrayList<Rectangle2D> ground, Rectangle2D spike, double tick)
	{
		for(int i = 0;i< ground.size() && present;i++)
		{
			if(ground.get(i).intersects(this.getCollisionBox()) )
			{
				present = false;
				hurty = false;
			}
		}
		this.x+=speed*(Math.cos(angle));
		this.y+=speed*(Math.sin(angle));
		return hit;
	}

}

class Bloop extends Enemy
{
	boolean move = false;
	
	ArrayList<Enemy> enemyList;
	Bloop(double x, double y, ArrayList<Enemy> enemyList)
	{
		this.width = 30;
		this.height = 40;
		this.x = x;
		this.y = y;
		this.direction = -1;
		this.enemyList = enemyList;
		faceset = new Image[9];
		this.faceset[0] = new Image("img/bloop/bloop1.png");
		this.faceset[1] = new Image("img/bloop/bloop2.png");
		this.faceset[2] = new Image("img/bloop/bloop3.png");
		this.faceset[3] = new Image("img/bloop/bloop4.png");
		this.faceset[4] = new Image("img/bloop/bloop5.png");
		this.faceset[5] = new Image("img/bloop/bloop6.png");
		this.faceset[6] = new Image("img/bloop/bloop7.png");
		this.faceset[7] = new Image("img/bloop/bloop8.png");
		this.faceset[8] = new Image("img/bloop/bloop9.png");
		this.face= faceset[0];
	}
	boolean update(ArrayList<Rectangle2D> ground, Rectangle2D spike, double tick)
	{
		if(this.getCollisionBox().intersects(spike))
		{
			present=false;
			hurty = false;
			hit=true;
		}
		move = false;
		if(tick%95==15)
		{
			enemyList.add(new Projectile(this.x,this.y+6,Math.atan2((spike.getMaxY()-this.y),(spike.getMaxX())- this.x),3.0));
		}
		
		if(!(tick%95<30))
		{
			if(tick%8==0)
			{
				if(cycle>5) 
					cycle = 0;
				face=faceset[cycle];
				cycle++;
			}
			for(int i = 0;i< ground.size() && !move;i++)
			{
				if(ground.get(i).contains(this.x+28,this.y+42) && direction == -1)
				{
					i=ground.size();
				}
				else if(ground.get(i).contains(this.x+32,this.y+42)	&& direction == -1)
				{
					this.y+=2;
					move=true;
				}
				else if(ground.get(i).contains(this.x+32,this.y-7) && direction == 1)
				{
					this.y-=2;
					move=true;
				}
			}
			if(!move)
			{
				direction = -direction;

			}
		}
		else
		{
			if(tick%7==0)
			{
				if(cycle<5 || cycle>8) 
					cycle = 5;
				face=faceset[cycle];
				cycle++;
			}
		}
		return hit;
	}
}

class Person
{
	int width = 40;
	int height = 40;
	double x;
	double y;
	int pos = 0;
	
	int health = 6; 
	
	int dir = 1;//+dir = right, -dir = left.
	int spikedir = 8;//uses numpad formatting for direction.
	Image[] face = new Image[8];
	Image currentface;
	Image[] spike = new Image[8];
	Image currentspike;
	double spikeX;
	double spikeY;
	
	
	Person()
	{
		face[0] = new Image("img/1r.png");
		face[1] = new Image("img/walk1r.png");
		face[2] = new Image("img/1r.png");
		face[3] = new Image("img/walk2r.png");
		face[4] = new Image("img/1l.png");
		face[5] = new Image("img/walk1l.png");
		face[6] = new Image("img/1l.png");
		face[7] = new Image("img/walk2l.png");
		
		spike[0] = new Image("img/spikeup.png");
		spike[1] = new Image("img/spikeright.png");
		spike[2] = new Image("img/spikeleft.png");
		spike[3] = new Image("img/spikedown.png");
		spike[4] = new Image("img/spikeupright.png");
		spike[5] = new Image("img/spikeupleft.png");
		spike[6] = new Image("img/spikedownright.png");
		spike[7] = new Image("img/spikedownleft.png");
		currentface = face[0];
		currentspike = spike[0];
		this.x = 0;
		this.y = 0;
		this.spikeX=this.x;
		this.spikeY=this.y-50;
	
	}
	Person(double x, double y)
	{
		face[0] = new Image("img/1r.png");
		face[1] = new Image("img/walk1r.png");
		face[2] = new Image("img/1r.png");
		face[3] = new Image("img/walk2r.png");
		face[4] = new Image("img/1l.png");
		face[5] = new Image("img/walk1l.png");
		face[6] = new Image("img/1l.png");
		face[7] = new Image("img/walk2l.png");
		
		spike[0] = new Image("img/spikeup.png");
		spike[1] = new Image("img/spikeright.png");
		spike[2] = new Image("img/spikeleft.png");
		spike[3] = new Image("img/spikedown.png");
		spike[4] = new Image("img/spikeupright.png");
		spike[5] = new Image("img/spikeupleft.png");
		spike[6] = new Image("img/spikedownright.png");
		spike[7] = new Image("img/spikedownleft.png");


		
		currentface = face[0];
		currentspike = spike[0];
		this.x = x;
		this.y = y;
		this.spikeX=this.x;
		this.spikeY=this.y-50;
	}	
	Image getImage()
	{
		return currentface;
	}
	double getx()
	{
		return x;
	}
	void healthHit()
	{
		this.health--;
	}
	int getHealth()
	{
		return health;
	}
	void setx(double x)
	{
		this.x = x;
		if(spikedir==8)
			this.spikeX=this.x;
		if(spikedir==6)
			this.spikeX=this.x+23;
		if(spikedir==4)
			this.spikeX=this.x-44;
		if(spikedir==2)
			this.spikeX=this.x+7;
		if(spikedir==9)
			this.spikeX=this.x+17;
		if(spikedir==7)
			this.spikeX=this.x-17;
		if(spikedir==1)
			this.spikeX=this.x-19;
		if(spikedir==3)
			this.spikeX=this.x+17;
	}
	void sety(double y)
	{
		this.y = y;
		if(spikedir==8)
			this.spikeY=this.y-50;
		if(spikedir==6 || spikedir==4)
			this.spikeY=this.y;
		if(spikedir==2)
			this.spikeY=this.y+19;
		if(spikedir==9 || spikedir==7)
			this.spikeY=this.y-26;
		if(spikedir==1 || spikedir == 3)
			this.spikeY=this.y+19;
		
	}
	double gety()
	{
		return y;
	}
	double getSpikeX()
	{
		return spikeX;
	}
	double getSpikeY()
	{
		return spikeY;
	}
	
	int getSpikeDir()
	{
		return spikedir;
	}
	
	Rectangle2D getBoundingBox()
	{
		return new Rectangle2D(this.x,this.y,this.width,this.height);
	}
	Rectangle2D getBoundingBoxFloor()
	{
		return new Rectangle2D(this.x+9,this.y+20,this.width-18,this.height-20);
	}
	Rectangle2D getBoundingBoxHead()
	{
		return new Rectangle2D(this.x+12,this.y,this.width-24,this.height-24);
	}
	Rectangle2D getBoundingBoxWall()
	{
		return new Rectangle2D(this.x,this.y,this.width,this.height-7);
	}
	Rectangle2D getBoundingBoxSpike()
	{
		if (spikedir == 2)
			return new Rectangle2D(this.spikeX+8,this.spikeY+47,10,10);
		else if (spikedir == 4)
			return new Rectangle2D(this.spikeX,this.spikeY+7,10,10);
		else if (spikedir == 6)
			return new Rectangle2D(this.spikeX+47,this.spikeY+7,10,10);
		else if (spikedir == 8)
			return new Rectangle2D(this.spikeX+15,this.spikeY,10,10);
		else if (spikedir == 9)
			return new Rectangle2D(this.spikeX+34,this.spikeY,10,10);
		else if (spikedir == 7)
			return new Rectangle2D(this.spikeX,this.spikeY,10,10);
		else if (spikedir == 1)
			return new Rectangle2D(this.spikeX,this.spikeY+32,10,10);
		else if (spikedir == 3)
			return new Rectangle2D(this.spikeX+40,this.spikeY+32,10,10);
		else
			return new Rectangle2D(0,0,7,7);

	
	}
	Rectangle2D getBoundingBoxSpikeDir(int testDir)
	{
		
		if (testDir == 2)
			return new Rectangle2D(this.x+15,this.y+66,10,10);
		else if (testDir == 4)
			return new Rectangle2D(this.x-44,this.y+7,10,10);
		else if (testDir == 6)
			return new Rectangle2D(this.x+70,this.y+7,10,10);
		else if (testDir == 8)
			return new Rectangle2D(this.x+15,this.y-50,10,10);
		else if (testDir == 9)
			return new Rectangle2D(this.x+51,this.y-26,10,10);
		else if (testDir == 7)
			return new Rectangle2D(this.x-17,this.y-26,10,10);
		else if (testDir == 1)
			return new Rectangle2D(this.x-19,this.y+51,10,10);
		else if (testDir == 3)
			return new Rectangle2D(this.x+57,this.y+51,10,10);
		else
			return new Rectangle2D(0,0,7,7);	
	}

	int getdir()
	{
		return dir;
	}
	Image getSpikeImage()
	{
		return currentspike;
	}
	void setSpikeDir(int direction)
	{
		this.spikedir = direction;
		if(spikedir == 2)
			this.currentspike = spike[3];
		if(spikedir == 4)
			this.currentspike = spike[2];
		if(spikedir == 6)
			this.currentspike = spike[1];
		if(spikedir == 8)
			this.currentspike = spike[0];
		if(spikedir == 9)
			this.currentspike = spike[4];
		if(spikedir == 7)
			this.currentspike = spike[5];
		if(spikedir == 1)
			this.currentspike = spike[7];
		if(spikedir == 3)
			this.currentspike = spike[6];
	}
	int getwidth()
	{
		return this.width;
	}
	void setDirAndRender(int dir)
	{
		if(this.dir<dir)
			currentface = face[0];
		else
			currentface = face[4];
		this.dir= dir;
	}
	void walk(double tick)
	{
		if(tick%6==0)
		{
			if(dir == 1)
			{
				pos++;
				if (pos>3) pos=0;
			}
			else if( dir == -1)
			{
				pos++;
				if (pos<4 || pos>7) pos = 4;
			}
			currentface = face[pos];
		}
	}
}
