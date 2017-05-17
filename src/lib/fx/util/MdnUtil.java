/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.fx.util;

/**
 *
 * @author febri
 */
public class MdnUtil {
    
    public static String format(String p_string) {
        try {
            if (p_string.startsWith("+62")) {
                p_string = p_string.substring(3);
            }
            else if (p_string.startsWith("62")) {
                p_string = p_string.substring(2);
            }
            else if (p_string.startsWith("0")) {
                p_string = p_string.substring(1);
            }
        }
        catch (Exception e) {
        }
        return p_string;
    }
    
}
