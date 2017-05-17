/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.fx.util;

/**
 *
 * @author Hian
 */
public class SpaceToken {

    private char[]        lexeme = null;
    private int           lexemeIndex = 0;
    private StringBuilder lexemesBuilder = null;
    private String[]      token = null;
    private int           tokenSize = 0;  

    public static final int SPACE = 32;
    public static final int HORIZONTALTAB = 9;
    public static final int FORMFEED = 12;
    public static final int NEWLINE = 10;
    public static final int CARRIAGERETURN = 13;
    public static final int DOUBLE_QUOTE = 34;
    
    public static final int[] WHITESPACE_INPUTS = new int[] {
        SPACE,
        HORIZONTALTAB,
        FORMFEED,
        NEWLINE,
        CARRIAGERETURN
    };
    
    public SpaceToken(String p_sentence) {
        if (p_sentence != null && !p_sentence.equals("")) {
            create(p_sentence);
        }
    }

    public final String getToken(int p_index) {
        return token[p_index];
    }

    public final int size() {
        return tokenSize;
    }
    
    public final boolean isEmpty() {
        return tokenSize == 0;
    }

    private void nextLexeme() {
        lexemeIndex++;
    }
    
    private void create(String p_sentence) {     
        lexemesBuilder = new StringBuilder();
        lexeme = p_sentence.toCharArray();
        token = new String[lexeme.length];
        while (lexemeIndex < lexeme.length) {
            if (isWhitespace()) {
                nextLexeme();
            } 
            else if (lexeme[lexemeIndex] == DOUBLE_QUOTE) {
                doublequoteStateMachine();
            } 
            else {
                letterStateMachine();
            }
        }        
    }

    private void doublequoteStateMachine() {
        nextLexeme();
        while (lexemeIndex < lexeme.length) {
            if (lexeme[lexemeIndex] == DOUBLE_QUOTE) {
                break;
            } 
            else {
                lexemesBuilder.append(lexeme[lexemeIndex]);
                nextLexeme();
            }
        }
        add(lexemesBuilder);
        nextLexeme();
    }

    private void letterStateMachine() {
        lexemesBuilder.append(lexeme[lexemeIndex]);
        nextLexeme();
        while (lexemeIndex < lexeme.length) {
            if (isWhitespace()) {
                break;
            }
            else {
                lexemesBuilder.append(lexeme[lexemeIndex]);
                nextLexeme();
            }
        }
        add(lexemesBuilder);
        nextLexeme();
    }

    private boolean isWhitespace() {
        for (int x = 0; x < WHITESPACE_INPUTS.length; x++) {
            if (lexeme[lexemeIndex] == WHITESPACE_INPUTS[x]) {
                return true;
            }
        }
        return false;
    }

    private void add(StringBuilder p_lexemesBuilder) {
        token[tokenSize] = p_lexemesBuilder.toString();
        lexemesBuilder.delete(0, lexemesBuilder.length());
        tokenSize++;
    }
        
//    public static void main(String[] args) {
//        
//        long T0 = System.currentTimeMillis();
//        for (int x = 0; x < 100000; x++) {
//            WsvBuilder wsv = new WsvBuilder("df gsadk jkssssssssssssssssssssssssssssssssssssssssssdaks dklasdjaslkasdadskldjlaskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkasl dadasd asdlkassd askd ad asdaskldasdlk asdklasjdlkasdklasdalsdasldasldkasldkskkkkkkkkdkaldkjalksdjaslkdjasldkasjdlkasjdlkasjdlask jdlskf kgj fg fgjk fk;k dfgk dfgkl df gdfk gdfkl flf jsdflk jsd;klf j;erjweifjsdlkfjsdlkfdjlfksjdfklj as  ktjgkl dfgkl dklg dgkl gkldf gdfkl gdkl gdfklg dfgkl dfgkl dgkldfgkldf gdfklg dfkl gdfklg dgkl dfgkldfgkldf gl rlj f sd adas dadas dasd  asdas d");
//        }
//        long T1 = System.currentTimeMillis();
//        System.out.println((T1 - T0) + "ms");
//        
//        long T0 = System.currentTimeMillis();
//        for (int x = 0; x < 100000; x++) {
//            String[] temp = "df gsadk jkssssssssssssssssssssssssssssssssssssssssssdaks dklasdjaslkasdadskldjlaskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkasl dadasd asdlkassd askd ad asdaskldasdlk asdklasjdlkasdklasdalsdasldasldkasldkskkkkkkkkdkaldkjalksdjaslkdjasldkasjdlkasjdlkasjdlask jdlskf kgj fg fgjk fk;k dfgk dfgkl df gdfk gdfkl flf jsdflk jsd;klf j;erjweifjsdlkfjsdlkfdjlfksjdfklj as  ktjgkl dfgkl dklg dgkl gkldf gdfkl gdkl gdfklg dfgkl dfgkl dgkldfgkldf gdfklg dfkl gdfklg dgkl dfgkldfgkldf gl rlj f sd adas dadas dasd  asdas d".split(" ");
//        }
//        long T1 = System.currentTimeMillis();
//        System.out.println((T1 - T0) + "ms");
//        
//    }
    
}