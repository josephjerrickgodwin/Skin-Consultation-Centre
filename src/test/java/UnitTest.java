import org.junit.Test;
import org.skin.consultation.management.cli.WestminsterSkinConsultationManager;
import org.skin.consultation.storage.Data;
import org.skin.consultation.users.doctor.Doctor;

import java.nio.charset.IllegalCharsetNameException;
import java.time.LocalDate;
import java.util.InputMismatchException;

import static org.junit.Assert.*;

public final class UnitTest {
    WestminsterSkinConsultationManager manager = new WestminsterSkinConsultationManager();

    @Test(expected = IllegalCharsetNameException.class)
    public void TEST_CASE_01() {
        System.out.println("""
                TEST CASE 01:
                
                Input           :  Tom64
                Expected Output :  java.nio.charset.IllegalCharsetNameException
                Description     :  Name/surname/specialisation cannot contain alphanumeric
                                   values
                """);
        manager.checkIllegalCharacters(1, "Tom64");
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void TEST_CASE_02() {
        System.out.println("""
                TEST CASE 02:
                
                Input           :  12345678
                Expected Output :  java.nio.charset.IllegalCharsetNameException
                Description     :  Name/surname/specialisation cannot contain Integer values
                """);
        manager.checkIllegalCharacters(1, "12345678");
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void TEST_CASE_03() {
        System.out.println("""
                TEST CASE 03:
                
                Input           :  T#om@Ho*lla^d=
                Expected Output :  java.nio.charset.IllegalCharsetNameException
                Description     :  Name/surname/specialisation should not contain illegal characters
                """);
        manager.checkIllegalCharacters(1, "T/om@Ho*lla^d=");
    }

    @Test
    public void TEST_CASE_04() {
        System.out.println("""
                TEST CASE 04:
                
                Input           :  Tom Holland
                Expected Output :  Tom Holland
                Description     :  Name/surname/specialisation should contain string values only
                """);
        assertEquals("Tom Holland" ,manager.checkIllegalCharacters(1, "Tom Holland"));
    }

    @Test(expected = InputMismatchException.class)
    public void TEST_CASE_05() {
        System.out.println("""
                TEST CASE 05:
                
                Input           :  0
                Expected Output :  java.util.InputMismatchException
                Description     :  Date cannot be 0 or a negative value
                """);
        manager.validateDob(1, 0);
    }

    @Test(expected = InputMismatchException.class)
    public void TEST_CASE_06() {
        System.out.println("""
                TEST CASE 06:
                
                Input           :  32
                Expected Output :  java.util.InputMismatchException
                Description     :  Date cannot exceed 31 days
                """);
        manager.validateDob(1, 32);
    }

    @Test
    public void TEST_CASE_07() {
        System.out.println("""
                TEST CASE 07:
                
                Input           :  12
                Expected Output :  12
                Description     :  Date should be within the range between 1 and 31 days
                """);
        assertEquals(12, manager.validateDob(1, 12));
    }

    @Test
    public void TEST_CASE_08() {
        System.out.println("""
                TEST CASE 08:
                
                Input           :  1
                Expected Output :  1
                Description     :  Month should start from 1 - January
                """);
        assertEquals(1, manager.validateDob(2, 1));
    }

    @Test(expected = InputMismatchException.class)
    public void TEST_CASE_09() {
        System.out.println("""
                TEST CASE 09:
                
                Input           :  13
                Expected Output :  java.util.InputMismatchException
                Description     :  Month should not exceed 12 - December
                """);
        manager.validateDob(2, 13);
    }

    @Test(expected = InputMismatchException.class)
    public void TEST_CASE_10() {
        System.out.println("""
                TEST CASE 10:
                
                Input           :  2006
                Expected Output :  java.util.InputMismatchException
                Description     :  Doctor must be at least 18 years old
                """);
        manager.validateDob(3, 2006);
    }

    @Test(expected = InputMismatchException.class)
    public void TEST_CASE_11() {
        System.out.println("""
                TEST CASE 11:
                
                Input           :  1957
                Expected Output :  java.util.InputMismatchException
                Description     :  Doctor must not be at his/her retirement age (<64)
                """);
        manager.validateDob(3, 1957);
    }

    @Test
    public void TEST_CASE_12() {
        System.out.println("""
                TEST CASE 12:
                
                Input           :  1990
                Expected Output :  1990
                Description     :  Doctor must be aged between 18 and 63
                """);
        assertEquals(1990, manager.validateDob(3, 1990));
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void TEST_CASE_13() {
        System.out.println("""
                TEST CASE 13:
                
                Input           :  0777roo777
                Expected Output :  java.nio.charset.IllegalCharsetNameException
                Description     :  Phone number should not contain string values
                """);
        manager.checkIllegalCharacters(3, "0777roo777");
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void TEST_CASE_14() {
        System.out.println("""
                TEST CASE 14:
                
                Input           :  077*777/7$5@10
                Expected Output :  java.nio.charset.IllegalCharsetNameException
                Description     :  Phone number should not contain illegal characters
                """);
        manager.checkIllegalCharacters(3, "077*777/7$5@10");
    }

    @Test
    public void TEST_CASE_15() {
        System.out.println("""
                TEST CASE 15:
                
                Input           :  0112233445
                Expected Output :  0112233445
                Description     :  Phone number should contain integer values only
                """);
        assertEquals("0112233445", manager.checkIllegalCharacters(2, "0112233445"));
    }

    @Test
    public void TEST_CASE_16() {
        System.out.println("""
                TEST CASE 16:
                
                Input           :  TOMH74700
                Expected Output :  True
                Description     :  A Doctor with a license number that of another doctor
                                   cannot be added. Returns true if the doctor has been added
                                   successfully
                """);
        // Create a doctor object
        Doctor doctor = new Doctor("Tom", "Holland", LocalDate.now(), "0112223334",
                "TOMH74700", "cosmetic dermatology");

        // Insert the object
        Data.getInstance().addDoctor(doctor.getLicense_number(), doctor);

        // Check whether doctor exists or not
        assertTrue(Data.getInstance().isDoctorExists("TOMH74700"));
    }

    @Test
    public void TEST_CASE_17() {
        System.out.println("""
                TEST CASE 17:
                
                Input           :  TOMH74700
                Expected Output :  False
                Description     :  Remove a doctor with the license number and check if
                                   the license number still exists on the system.
                                   Returns false if the doctor does not exists with the
                                   license number
                """);
        // Remove the previous doctor with the license number
        Data.getInstance().removeDoctor("TOMH74700");

        // Validate if the doctor still exists on the system
        assertFalse(Data.getInstance().isDoctorExists("TOMH74700"));
    }
}