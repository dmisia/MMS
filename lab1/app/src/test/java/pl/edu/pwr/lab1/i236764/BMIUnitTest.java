package pl.edu.pwr.lab1.i236764;

import org.junit.Test;

import static org.junit.Assert.*;

public class BMIUnitTest {
    @Test
    public void bmi_kg_cm_isOverweight() {
        BMI bmi = new BMI(80,170);

        assertEquals(27.68, bmi.getBmi(), 0.01);
        assertEquals(R.color.red_700, bmi.getAppropriateColor());
    }
    @Test
    public void bmi_kg_cm_isCorrect() {
        BMI bmi = new BMI(55,165);

        assertEquals(20.20, bmi.getBmi(), 0.01);
        assertEquals(R.color.teal_700, bmi.getAppropriateColor());
    }
    @Test
    public void bmi_lbs_inch_isUnderweight() {
        MainActivity.metricsInKgCm = false;
        BMI bmi = new BMI(104.04, 63.02);

        assertEquals(18.42, bmi.getBmi(), 0.01);
        assertEquals(R.color.red_700, bmi.getAppropriateColor());
    }
}