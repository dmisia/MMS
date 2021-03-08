package pl.edu.pwr.lab1.i236764;


public class BMI {

    private double bmi;
    private double mass;
    private double height;

    public BMI(double mass, double height) {
        this.mass = mass;
        this.height = height;
        this.bmi = calculateBMI();
    }

    int getAppropriateColor() {
        if  (this.bmi > 18.5 && this.bmi < 25d) {
            return R.color.teal_700;
        }
        else
            return R.color.red_700;
    }

    private double calculateBMI() {
        if(MainActivity.metricsInKgCm) {
            return this.mass / (this.height * this.height) * 10000;
        }
        else{
            return (toLbs(this.mass) ) / (toInch(this.height) * toInch(this.height)) * 10000;
        }
    }

    private double toInch(Double height) {
        return height * 2.54;
    }

    private double toLbs(Double mass) {
        return mass * 0.453592;
    }

    public double getBmi() {
        return bmi;
    }

}
