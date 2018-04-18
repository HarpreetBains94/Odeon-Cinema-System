/*
 * 
 * 
 */
public class Screen {
    private String screenId;
    private static int screenCount = 0;

    public Screen(){
        screenCount++;
        screenId =String.valueOf(screenCount);
    }

    public String getScreenId(){
        return screenId;
    }
}
