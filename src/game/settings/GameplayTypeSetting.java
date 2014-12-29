package game.settings;

public class GameplayTypeSetting {
    private static GameplayType gameplayType = GameplayType.TWO_HANDED;
    
    public static GameplayType gameplayType() {
        return gameplayType;
    }
    
    public static void setGameplayType( GameplayType gameplayType ) {
        GameplayTypeSetting.gameplayType = gameplayType;
    }
}
