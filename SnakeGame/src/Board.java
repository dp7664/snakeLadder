import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener{
    int B_HEIGHT=400;
    int B_WIDTH=400;

    int MAX_DOTS=1600;
    int DOT_SIZE=10;
    int DOTS;
    int X[]=new int[MAX_DOTS];
    int Y[]=new int[MAX_DOTS];

    int apple_x;
    int apple_y;

    //images
    Image body,head,apple;
    Timer timer;
    int DELAY=150;

    boolean leftDirection=true;
    boolean rightDirection=false;
    boolean upDirection=false;
    boolean downDirection=false;

    boolean inGame=true;

    Board(){
        TAdapter tAdapter=new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH,B_HEIGHT));
        setBackground(Color.BLACK);
        initGame();
        LoadImages();
    }
    public void initGame(){
       DOTS=3;
       //initialize the snake position
       X[0]=250;
       Y[0]=250;
       for(int i=1;i<DOTS;i++){
           X[i]=X[0]+DOT_SIZE*i;
           Y[i]=Y[0];
       }
     //initialize apple position
        locateApple();
     timer=new Timer(DELAY,this);
     timer.start();
    }
    public void LoadImages(){
        ImageIcon bodyIcon=new ImageIcon("src/Resources/dot.png");
        body=bodyIcon.getImage();
        ImageIcon headIcon=new ImageIcon("src/Resources/head.png");
        head=headIcon.getImage();
        ImageIcon appleIcon=new ImageIcon("src/Resources/apple.png");
        apple=appleIcon.getImage();
    }
    //draw the image at snake and apple position
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }
    //draw image
    public void doDrawing(Graphics g){
        if(inGame){
            g.drawImage(apple,apple_x,apple_y,this);
            for(int i=0;i<DOTS;i++){
                if(i==0){
                    g.drawImage(head,X[0],Y[0],this);
                }
                else
                    g.drawImage(body,X[i],Y[i],this);
            }
        }
        else{
            gameOver(g);
            timer.stop();
        }


        }

    //randomise apple position
    public void locateApple(){
        apple_x=((int)(Math.random()*39))*DOT_SIZE;
        apple_y=((int)(Math.random()*39))*DOT_SIZE;
    }
    //check collision with body and border
    public void checkCollision(){
        //collision with  body
        for(int i=1;i<DOTS;i++){
            if(i>4 && X[0]==X[i]&& Y[0]==Y[i]){
                inGame=false;
            }
        }
        //collision with border
        if(X[0]<0){
            inGame=false;
        }
        if(X[0]>=B_WIDTH){
            inGame=false;
        }
        if(Y[0]<0){
            inGame=false;
        }
        if(Y[0]>=B_HEIGHT){
            inGame=false;
        }
    }

    //game over  message
    public void gameOver(Graphics g){
        String msg="Game Over";
        int score=(DOTS-3)*100;
        String scoremsg="score:"+Integer.toString(score);
        Font small=new Font("Helvetica",Font.BOLD,28);
        FontMetrics fontMetrics=getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg,(B_WIDTH-fontMetrics.stringWidth(msg))/2,B_HEIGHT/4);
        g.drawString(scoremsg,(B_WIDTH-fontMetrics.stringWidth(scoremsg))/2,3*(B_HEIGHT/4));
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent){
    if(inGame){
        checkApple();
        checkCollision();
        move();
    }


    repaint();

    }
    public void move(){
        for(int i=DOTS-1;i>0;i--){
            X[i]=X[i-1];
            Y[i]=Y[i-1];
        }
        if(leftDirection){
            X[0]-=DOT_SIZE;
        }
        if(rightDirection){
            X[0]+=DOT_SIZE;
        }
        if(upDirection){
            Y[0]-=DOT_SIZE;
        }
        if(downDirection){
            Y[0]+=DOT_SIZE;
        }
    }

    //make snake eater
    public void checkApple(){
        if(apple_x==X[0]&&apple_y==Y[0]){
            DOTS++;
            locateApple();
        }
    }


    //implement control
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key=keyEvent.getKeyCode();
            if(key==KeyEvent.VK_LEFT&&!rightDirection){
                leftDirection=true;
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_RIGHT&&!leftDirection){
                rightDirection=true;
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_UP&&!downDirection){
                upDirection=true;
                leftDirection=false;
                rightDirection=false;
            }
            if(key==KeyEvent.VK_DOWN&&!upDirection){
                downDirection=true;
                leftDirection=false;
                rightDirection=false;
            }
        }
    }
}
