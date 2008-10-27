/*
 * StringUtilsTest.java
 * JMUnit based test
 *
 * Created on Oct 26, 2008, 1:26:33 AM
 */
package net.friendtracker.client.j2me.util;

import java.util.Vector;
import jmunit.framework.cldc10.*;

/**
 * @author rafael
 */
public class StringUtilsTest extends TestCase {

    public StringUtilsTest() {
        //The first parameter of inherited constructor is the number of test cases
        super(3, "StringUtilsTest");
    }

    public void test(int testNumber) throws Throwable {
        switch (testNumber) {
            case 0:
                testGetRandomMapColorFromString();
                break;
            case 1:
                testSplitStr();
                break;
            case 2:
                testSplit();
                break;
            default:
                break;
        }
    }

    /**
     * Test of testGetRandomMapColorFromString method, of class StringUtils.
     */
    public void testGetRandomMapColorFromString() throws AssertionFailedException {
        System.out.println("getRandomMapColorFromString");
        int result_1 = StringUtils.getRandomMapColorFromString("mufumbo");
        int result_2 = StringUtils.getRandomMapColorFromString("othername");
        assertNotEquals(result_1, result_2);
    }

    /**
     * Test of testSplitStr method, of class StringUtils.
     */
    public void testSplitStr() throws AssertionFailedException {
        System.out.println("splitStr");
        String original_1 = "1,2,3";
        String separator_1 = ",";
        String[] expResult_1 = new String[]{"1", "2", "3"};
        String[] result_1 = StringUtils.splitStr(original_1, separator_1);
        assertEquals(expResult_1, result_1);
    }

    /**
     * Test of testSplit method, of class StringUtils.
     */
    public void testSplit() throws AssertionFailedException {
        System.out.println("split");
        String original_1 = "1,2,3";
        String separator_1 = ",";
        Vector expResult_1 = new Vector();
        expResult_1.addElement("1");
        expResult_1.addElement("2");
        expResult_1.addElement("3");
        Vector result_1 = StringUtils.split(original_1, separator_1);
        assertEquals(expResult_1, result_1);
    }

    public static void main(String[] args) {
        StringUtilsTest sut = new StringUtilsTest();
    }
}
