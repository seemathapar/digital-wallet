package solution;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Test;

public class AntiFraudTest {

	
	AntiFraud classUnderTest = new AntiFraud();
	
	@Test
	public void testValidateFile_FileDoesNotExist() {
		assertEquals(null, classUnderTest.validateFile("test1.txt"));
	}
	
	@Test
	public void testValidateFile_EmptyFileExist_return_null() {
		assertEquals(null, classUnderTest.validateFile("data/test2.txt"));
	}
	
	@Test
	public void testValidateFile_FileExistButOnly1HeaderRow_return_null() {
		assertEquals(null, classUnderTest.validateFile("data/test3.txt"));
	}
	
	@Test
	public void testValidateFile_buildPaymentHistoryNetwork_return_NumberOfVertices() {
		classUnderTest.buildPaymentHistoryGraph("data/test4.txt");
		assertEquals(10, classUnderTest.getGraph().getNumberOfVertices());
	}
	
	@Test
	public void testValidateFile_buildPaymentHistoryNetworkWithDuplicateEntries_return_NumberOfVertices() {
		classUnderTest.buildPaymentHistoryGraph("data/test5.txt");
		assertEquals(10, classUnderTest.getGraph().getNumberOfVertices());
	}
	
	
	@Test
	public void test_isFriend_ReturnsTrue_IfConnectedByLevel1() {
		classUnderTest.buildPaymentHistoryGraph("data/test5.txt");
		assertEquals(true, classUnderTest.isFriend("A", "B"));
		assertEquals(true, classUnderTest.isFriend("B", "C"));
		assertEquals(true, classUnderTest.isFriend("E", "F"));
	}
	
	@Test
	public void test_isFriend_ReturnsFalse_IfConnectedByLevelGreaterThanLevel1() {
		classUnderTest.buildPaymentHistoryGraph("data/test5.txt");
		assertEquals(false, classUnderTest.isFriend("A", "I"));
		assertEquals(false, classUnderTest.isFriend("B", "F"));
		assertEquals(false, classUnderTest.isFriend("E", "C"));
	}
	
	
	@Test
	public void test_isFriend_ReturnsTrue_IfConnectedByLevel2() {
		classUnderTest.buildPaymentHistoryGraph("data/test5.txt");
		assertEquals(true, classUnderTest.isNLevelFriend("B", "F",2));
	}
	
	@Test
	public void test_isFriend_ReturnsFalse_IfConnectedByLevel2() {
		classUnderTest.buildPaymentHistoryGraph("data/test5.txt");
		
		assertEquals(false, classUnderTest.isNLevelFriend("E", "C",2));
		assertEquals(false, classUnderTest.isNLevelFriend("A", "I",2));
	}
	
	@Test
	public void test_isFriend_ReturnsTrue_IfConnectedByLevel4() {
		classUnderTest.buildPaymentHistoryGraph("data/test5.txt");
		assertEquals(true, classUnderTest.isNLevelFriend("B", "F",4));
		assertEquals(true, classUnderTest.isNLevelFriend("E", "C",4));
	}
	
	@Test
	public void test_isFriend_ReturnsFalse_IfConnectedByLevel4() {
		classUnderTest.buildPaymentHistoryGraph("data/test5.txt");
		
		assertEquals(false, classUnderTest.isNLevelFriend("A", "I",4));
	}
	
	
	@Test
	public void test_BitSet() {
		BitSet bitset = new BitSet(0);
		bitset.set(0,false);
		bitset.set(1,false);
		bitset.set(2,false);
		bitset.set(3,false);
		bitset.set(4,false);
		System.out.println(bitset.length());
	}
	
	

}
