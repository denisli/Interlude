package game.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import music.Note;

import org.newdawn.slick.Input;

import util.Pair;

public class Controls {
    private static final List<Integer> NOTES_IN_ORDER = new ArrayList<Integer>(Arrays.asList(
            Note.A,
            Note.B,
            Note.C,
            Note.D,
            Note.E,
            Note.F,
            Note.G
            ));
    
    // single voice controls
    private static int ANoteKey = Input.KEY_A;
    private static int BNoteKey = Input.KEY_S;
    private static int CNoteKey = Input.KEY_D;
    private static int DNoteKey = Input.KEY_F;
    private static int ENoteKey = Input.KEY_J;
    private static int FNoteKey = Input.KEY_K;
    private static int GNoteKey = Input.KEY_L;

    // two voice controls
    private static int leftANoteKey = Input.KEY_A;
    private static int leftBNoteKey = Input.KEY_S;
    private static int leftCNoteKey = Input.KEY_D;
    private static int leftDNoteKey = Input.KEY_F;
    private static int leftENoteKey = Input.KEY_G;
    private static int leftFNoteKey = Input.KEY_W;
    private static int leftGNoteKey = Input.KEY_E;

    private static int rightANoteKey = Input.KEY_H;
    private static int rightBNoteKey = Input.KEY_J;
    private static int rightCNoteKey = Input.KEY_K;
    private static int rightDNoteKey = Input.KEY_L;
    private static int rightENoteKey = Input.KEY_SEMICOLON;
    private static int rightFNoteKey = Input.KEY_U;
    private static int rightGNoteKey = Input.KEY_I;
    
    public static List<Integer> notesInOrder() {
        return NOTES_IN_ORDER;
    }
    
    public static int[] noteKeys( GameplayType gameplayType ) {
        if ( gameplayType == GameplayType.ONE_HANDED ) {
            return new int[] { ANoteKey, BNoteKey, CNoteKey, DNoteKey, 
                    ENoteKey, FNoteKey, GNoteKey
            };
        } else if ( gameplayType == GameplayType.TWO_HANDED ) {
            return new int[] { leftANoteKey, leftBNoteKey, leftCNoteKey, leftDNoteKey,
                    leftENoteKey, leftFNoteKey, leftGNoteKey, 
                    rightANoteKey, rightBNoteKey, rightCNoteKey, rightDNoteKey,
                    rightENoteKey, rightFNoteKey, rightGNoteKey 
            };
        } else {
            throw new IllegalArgumentException("Valid voice type not given");
        }
    }
    
    public static void setKey( int note, Handedness handedness, int key ) {
        if ( handedness == Handedness.SINGLE ) {
            if ( note == Note.A ) {
                setANoteKey( key );
            } else if ( note == Note.B ) {
                setBNoteKey( key );
            } else if ( note == Note.C ) {
                setCNoteKey( key );
            } else if ( note == Note.D ) {
                setDNoteKey( key );
            } else if ( note == Note.E ) {
                setENoteKey( key );
            } else if ( note == Note.F ) {
                setFNoteKey( key );
            } else if ( note == Note.G ) {
                setGNoteKey( key );
            } else {
                throw new IllegalArgumentException( "Valid letter for note not given" );
            }
        } else if ( handedness == Handedness.LEFT ) {
            if ( note == Note.A ) {
                setLeftANoteKey( key );
            } else if ( note == Note.B ) {
                setLeftBNoteKey( key );
            } else if ( note == Note.C ) {
                setLeftCNoteKey( key );
            } else if ( note == Note.D ) {
                setLeftDNoteKey( key );
            } else if ( note == Note.E ) {
                setLeftENoteKey( key );
            } else if ( note == Note.F ) {
                setLeftFNoteKey( key );
            } else if ( note == Note.G ) {
                setLeftGNoteKey( key );
            } else {
                throw new IllegalArgumentException( "Valid letter for note not given" );
            }
        } else if ( handedness == Handedness.RIGHT ) {
            if ( note == Note.A ) {
                setRightANoteKey( key );
            } else if ( note == Note.B ) {
                setRightBNoteKey( key );
            } else if ( note == Note.C ) {
                setRightCNoteKey( key );
            } else if ( note == Note.D ) {
                setRightDNoteKey( key );
            } else if ( note == Note.E ) {
                setRightENoteKey( key );
            } else if ( note == Note.F ) {
                setRightFNoteKey( key );
            } else if ( note == Note.G ) {
                setRightGNoteKey( key );
            } else {
                throw new IllegalArgumentException( "Valid letter for note not given" );
            }
        } else {
            throw new IllegalArgumentException( "Valid voice type not given" );
        }
    }
    
    public static Pair<Integer, Handedness> correspondingLetterAndHand(int key) {
		if ( GameplayTypeSetting.gameplayType() == GameplayType.ONE_HANDED ) {
			Handedness handedness = Handedness.SINGLE;
			if ( key == ANoteKey ) {
                return new Pair<Integer,Handedness>(Note.A,handedness);
			} else if ( key == BNoteKey ) {
                return new Pair<Integer,Handedness>(Note.B,handedness);
			} else if ( key == CNoteKey ) {
                return new Pair<Integer,Handedness>(Note.C,handedness);
			} else if ( key == DNoteKey ) {
                return new Pair<Integer,Handedness>(Note.D,handedness);
			} else if ( key == ENoteKey ) {
                return new Pair<Integer,Handedness>(Note.E,handedness);
			} else if ( key == FNoteKey ) {
                return new Pair<Integer,Handedness>(Note.F,handedness);
			} else if ( key == GNoteKey ) {
                return new Pair<Integer,Handedness>(Note.G,handedness);
			} else {
                throw new IllegalArgumentException("Key given is not a note key");
			}
		} else {
			if ( key == leftANoteKey ) {
                return new Pair<Integer,Handedness>(Note.A,Handedness.LEFT);
			} else if ( key == leftBNoteKey ) {
                return new Pair<Integer,Handedness>(Note.B,Handedness.LEFT);
			} else if ( key == leftCNoteKey ) {
                return new Pair<Integer,Handedness>(Note.C,Handedness.LEFT);
			} else if ( key == leftDNoteKey ) {
				return new Pair<Integer,Handedness>(Note.D,Handedness.LEFT);
			} else if ( key == leftENoteKey ) {
                return new Pair<Integer,Handedness>(Note.E,Handedness.LEFT);
			} else if ( key == leftFNoteKey ) {
                return new Pair<Integer,Handedness>(Note.F,Handedness.LEFT);
			} else if ( key == leftGNoteKey ) {
                return new Pair<Integer,Handedness>(Note.G,Handedness.LEFT);
			} else if ( key == rightANoteKey ) {
				return new Pair<Integer,Handedness>(Note.A,Handedness.RIGHT);
			} else if ( key == rightBNoteKey ) {
				return new Pair<Integer,Handedness>(Note.B,Handedness.RIGHT);
			} else if ( key == rightCNoteKey ) {
				return new Pair<Integer,Handedness>(Note.C,Handedness.RIGHT);
			} else if ( key == rightDNoteKey ) {
				return new Pair<Integer,Handedness>(Note.D,Handedness.RIGHT);
			} else if ( key == rightENoteKey ) {
				return new Pair<Integer,Handedness>(Note.E,Handedness.RIGHT);
			} else if ( key == rightFNoteKey ) {
				return new Pair<Integer,Handedness>(Note.F,Handedness.RIGHT);
			} else if ( key == rightGNoteKey ) {
				return new Pair<Integer,Handedness>(Note.G,Handedness.RIGHT);
			} else {
				throw new IllegalArgumentException("Key given is not a note key");
			}
		}
	}

    public static int correspondingKey(int letter, Handedness handedness ) {
        if ( handedness == Handedness.SINGLE ) {
            if (letter == Note.A) {
                return ANoteKey;
            } else if (letter == Note.B) {
                return BNoteKey;
            } else if (letter == Note.C) {
                return CNoteKey;
            } else if (letter == Note.D) {
                return DNoteKey;
            } else if (letter == Note.E) {
                return ENoteKey;
            } else if (letter == Note.F) {
                return FNoteKey;
            } else if (letter == Note.G) {
                return GNoteKey;
            } else {
                throw new IllegalArgumentException("A valid note was not given!");
            }
        } else if ( handedness == Handedness.LEFT ){
            if (letter == Note.A) {
                return leftANoteKey;
            } else if (letter == Note.B) {
                return leftBNoteKey;
            } else if (letter == Note.C) {
                return leftCNoteKey;
            } else if (letter == Note.D) {
                return leftDNoteKey;
            } else if (letter == Note.E) {
                return leftENoteKey;
            } else if (letter == Note.F) {
                return leftFNoteKey;
            } else if (letter == Note.G) {
                return leftGNoteKey;
            } else {
                throw new IllegalArgumentException("A valid note was not given!");
            }
        } else if ( handedness == Handedness.RIGHT ){
            if (letter == Note.A) {
                return rightANoteKey;
            } else if (letter == Note.B) {
                return rightBNoteKey;
            } else if (letter == Note.C) {
                return rightCNoteKey;
            } else if (letter == Note.D) {
                return rightDNoteKey;
            } else if (letter == Note.E) {
                return rightENoteKey;
            } else if (letter == Note.F) {
                return rightFNoteKey;
            } else if (letter == Note.G) {
                return rightGNoteKey;
            } else {
                throw new IllegalArgumentException("A valid note was not given!");
            }
        } else {
            throw new IllegalArgumentException("Not a valid voice type");
        }
    }
    
    
    /**
     * Changes the order of the notes that appear in a round via swapping a note's position.
     * @param positionMoved
     *          the rank of the note moved (0 rank is leftmost/topmost, 8 rank is rightmost/bottommost)
     * @param newPosition
     *          the rank to move the note into (0 rank is leftmost/topmost, 8 rank is rightmost/bottommost)
     */
    public static void changeNoteOrder( int positionMoved, int newPosition ) {
        int noteIntegerRemoved = NOTES_IN_ORDER.remove( positionMoved );
        NOTES_IN_ORDER.add( newPosition, noteIntegerRemoved );
    }
    
    // one voice key getters and setters
    public static int getANoteKey() {
        return ANoteKey;
    }

    public static void setANoteKey(int aNoteKey) {
        ANoteKey = aNoteKey;
    }

    public static int getBNoteKey() {
        return BNoteKey;
    }

    public static void setBNoteKey(int bNoteKey) {
        BNoteKey = bNoteKey;
    }

    public static int getCNoteKey() {
        return CNoteKey;
    }

    public static void setCNoteKey(int cNoteKey) {
        CNoteKey = cNoteKey;
    }

    public static int getDNoteKey() {
        return DNoteKey;
    }

    public static void setDNoteKey(int dNoteKey) {
        DNoteKey = dNoteKey;
    }

    public static int getENoteKey() {
        return ENoteKey;
    }

    public static void setENoteKey(int eNoteKey) {
        ENoteKey = eNoteKey;
    }

    public static int getFNoteKey() {
        return FNoteKey;
    }

    public static void setFNoteKey(int fNoteKey) {
        FNoteKey = fNoteKey;
    }

    public static int getGNoteKey() {
        return GNoteKey;
    }

    public static void setGNoteKey(int gNoteKey) {
        GNoteKey = gNoteKey;
    }

    // two voice key getters and setters
    public static int getLeftANoteKey() {
        return leftANoteKey;
    }

    public static void setLeftANoteKey(int leftANoteKey) {
        Controls.leftANoteKey = leftANoteKey;
    }

    public static int getLeftBNoteKey() {
        return leftBNoteKey;
    }

    public static void setLeftBNoteKey(int leftBNoteKey) {
        Controls.leftBNoteKey = leftBNoteKey;
    }

    public static int getLeftCNoteKey() {
        return leftCNoteKey;
    }

    public static void setLeftCNoteKey(int leftCNoteKey) {
        Controls.leftCNoteKey = leftCNoteKey;
    }

    public static int getLeftDNoteKey() {
        return leftDNoteKey;
    }

    public static void setLeftDNoteKey(int leftDNoteKey) {
        Controls.leftDNoteKey = leftDNoteKey;
    }

    public static int getLeftENoteKey() {
        return leftENoteKey;
    }

    public static void setLeftENoteKey(int leftENoteKey) {
        Controls.leftENoteKey = leftENoteKey;
    }

    public static int getLeftFNoteKey() {
        return leftFNoteKey;
    }

    public static void setLeftFNoteKey(int leftFNoteKey) {
        Controls.leftFNoteKey = leftFNoteKey;
    }

    public static int getLeftGNoteKey() {
        return leftGNoteKey;
    }

    public static void setLeftGNoteKey(int leftGNoteKey) {
        Controls.leftGNoteKey = leftGNoteKey;
    }

    public static int getRightANoteKey() {
        return rightANoteKey;
    }

    public static void setRightANoteKey(int rightANoteKey) {
        Controls.rightANoteKey = rightANoteKey;
    }

    public static int getRightBNoteKey() {
        return rightBNoteKey;
    }

    public static void setRightBNoteKey(int rightBNoteKey) {
        Controls.rightBNoteKey = rightBNoteKey;
    }

    public static int getRightCNoteKey() {
        return rightCNoteKey;
    }

    public static void setRightCNoteKey(int rightCNoteKey) {
        Controls.rightCNoteKey = rightCNoteKey;
    }

    public static int getRightDNoteKey() {
        return rightDNoteKey;
    }

    public static void setRightDNoteKey(int rightDNoteKey) {
        Controls.rightDNoteKey = rightDNoteKey;
    }

    public static int getRightENoteKey() {
        return rightENoteKey;
    }

    public static void setRightENoteKey(int rightENoteKey) {
        Controls.rightENoteKey = rightENoteKey;
    }

    public static int getRightFNoteKey() {
        return rightFNoteKey;
    }

    public static void setRightFNoteKey(int rightFNoteKey) {
        Controls.rightFNoteKey = rightFNoteKey;
    }

    public static int getRightGNoteKey() {
        return rightGNoteKey;
    }

    public static void setRightGNoteKey(int rightGNoteKey) {
        Controls.rightGNoteKey = rightGNoteKey;
    }

    public static void resetKeys() {
        ANoteKey = Input.KEY_A;
        BNoteKey = Input.KEY_S;
        CNoteKey = Input.KEY_D;
        DNoteKey = Input.KEY_F;
        ENoteKey = Input.KEY_J;
        FNoteKey = Input.KEY_K;
        GNoteKey = Input.KEY_L;
    }
}
