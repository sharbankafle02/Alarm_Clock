import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
class AlarmClock implements Runnable{
    private final LocalTime alarmTime;
    private final String Filepath;
    private final Scanner scanner;
    AlarmClock(LocalTime alarmTime,String Filepath,Scanner scanner){
        this.alarmTime=alarmTime;
        this.Filepath=Filepath;
        this.scanner=scanner;
    }
    @Override
    public void run(){
        while(LocalTime.now().isBefore(alarmTime)){
            try{
                Thread.sleep(1000);
                LocalTime now=LocalTime.now();
                int hour=now.getHour();
                int minute=now.getMinute();
                int second=now.getSecond();
                System.out.printf("\r%02d:%02d:%02d",hour,minute,second);
            }
            catch(InterruptedException e){
                System.out.println("OOPS!! The main file is intruptted");
            }
        }
        makeSound();
    }
    private void makeSound(){
        File file=new File(Filepath);
        try(AudioInputStream audioStream=AudioSystem.getAudioInputStream(file)){
            Clip clip=AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            System.out.println("\nPress *Enter* to stop alarm: ");
            scanner.nextLine();
            clip.stop();
            scanner.close();
        }
        catch(UnsupportedAudioFileException e){
            System.out.println("OOPS! The audio file is not supproted");
        }
        catch(LineUnavailableException e){
            System.out.println("OOPS!! The audio line is not supported");
        }
        catch(IOException e){
            System.out.println("OOPS!! Sonething went wrong");
        }
    }
}

public class Main{
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime alarmTime=null;
        String filepath="Delulu Dancer - The Soundlings.wav";
        while(alarmTime==null){
            try{
                System.out.println("Enter the time for alarm: ");
                String input=scanner.nextLine();
                alarmTime=LocalTime.parse(input,formatter);
                System.out.println("The time is set for "+alarmTime);
            }
            catch(DateTimeParseException e){
                System.out.println("OOPS!! please enter the time in valid formate");
            }
        }
        AlarmClock alarmClock=new AlarmClock(alarmTime,filepath,scanner);
        Thread  alarmThread=new Thread(alarmClock);
        alarmThread.start();  
    }
}