import javax.swing.*;
import java.awt.*;
import java.applet.*;
import java.net.*;



public class Simon extends Thread
{
    private final int MAXLENGTH = 12;
    private final int TIMEOUT = 3000;
    private final String SOUNDBASE = "file:///C:/soundbank";
   
    private int[] sequence = new int[MAXLENGTH];
    private int response;
    
    
    private AudioClip buttonClips[] = new AudioClip[4];
    private AudioClip ding, win, lose;
    
    private JButton[] buttons = new JButton[4];
    private Color[] buttonColors = new Color[4];
    private Color[] brighterColors = new Color[4];
    private JLabel label0;
    private JLabel label1;
    private JButton btnStart;
    
    private int repsonse;
    
    public Simon(JButton btn0, JButton btn1, JButton btn2, JButton btn3,
                 JLabel label0, JLabel label1, JButton btnStart)
    {
        buttons[0] = btn0;
        buttons[1] = btn1;
        buttons[2] = btn2;
        buttons[3] = btn3;
        
        buttonColors[0] = buttons[0].getBackground();
        buttonColors[1] = buttons[1].getBackground();
        buttonColors[2] = buttons[2].getBackground();
        buttonColors[3] = buttons[3].getBackground();
        
        brighterColors[0] = buttonColors[0].brighter().brighter();
        brighterColors[1] = buttonColors[1].brighter().brighter();
        brighterColors[2] = buttonColors[2].brighter().brighter();
        brighterColors[3] = buttonColors[3].brighter().brighter();
        
        this.label0 = label0;
        this.label1 = label1;
        this.btnStart = btnStart;
        try
        {
            // a different sound could be applied to each button
            buttonClips[0] = Applet.newAudioClip(new URL(SOUNDBASE + "radarping.wav"));
            buttonClips[1] = Applet.newAudioClip(new URL(SOUNDBASE + "radarping.wav"));
            buttonClips[2] = Applet.newAudioClip(new URL(SOUNDBASE + "radarping.wav"));
            buttonClips[3] = Applet.newAudioClip(new URL(SOUNDBASE + "radarping.wav"));
            ding = Applet.newAudioClip(new URL(SOUNDBASE + "ding.wav"));
            win = Applet.newAudioClip(new URL(SOUNDBASE + "game_win.au"));
            lose = Applet.newAudioClip(new URL(SOUNDBASE + "buzzthruloud.wav"));
        }
        catch (MalformedURLException e)
        {
            System.out.println("Error loading audio clips");
            e.printStackTrace();
        }
    }
    
    public void run()
    {
        long loopTime;
        boolean success = true;
        
        
        // generate a squence of random numbers between 0 and 3
        for (int i=0; i<MAXLENGTH; i++)
            sequence[i] = (int)(Math.random()*4);

        // display the sequence to the player
        int index;
        for (int length=1; length<=MAXLENGTH&&success; length++)
        {
            buttons[0].setEnabled(false);
            buttons[1].setEnabled(false);
            buttons[2].setEnabled(false);
            buttons[3].setEnabled(false);
            
            label0.setForeground(Color.blue);
            label0.setText("Round " + length);
            label1.setText("");
            ding.play();
            delay(2000);
            label1.setForeground(Color.blue);
            label1.setText("");
            
            // display sequence
            for (int count=0; count<length; count++)
            {
                index = sequence[count];
                buttons[index].setBackground(brighterColors[index]);
                buttonClips[index].play();
                delay(600-length*25);
                buttons[index].setBackground(buttonColors[index]);
                delay(600-length*25);
            }
            
            buttons[0].setEnabled(true);
            buttons[1].setEnabled(true);
            buttons[2].setEnabled(true);
            buttons[3].setEnabled(true);
            
            
            // test player response
            for (int count=0; count<length; count++)
            {
                response = -1;
                loopTime = System.currentTimeMillis() + TIMEOUT;
                
                while (response == -1 && 
                       System.currentTimeMillis() < loopTime)
                    delay(50);
       
                if (response != sequence[count])
                {
                    success = false;
                    break;
                }
            }
            
            delay(1500);
        }
        
        if (success)
        {
            label0.setForeground(Color.white);
            label0.setText("Congratulation");
            label1.setForeground(Color.white);
            label1.setText("Excellent Memory");
            win.play();
        }
        else
        {
            label0.setForeground(Color.red);
            label0.setText("Sorry");
            label1.setForeground(Color.red);
            label1.setText("Try Again");
            lose.play();
        }
        
        btnStart.setEnabled(true);
    }
    
    public void setResponse(int index)
    {
        response = index;
        buttonClips[index].play();
    }
    
    
    private void delay(long milliseconds)
    {
        try
        {
            sleep(milliseconds);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    
    
}
