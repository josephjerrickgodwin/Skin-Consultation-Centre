package org.skin.consultation.management.gui.calculator;

/**
 * A class with predefined constants related to patients
 * Details regarding the cost calculation according to the
 * User types.
 *
 * @author Joseph Jerrick Godwin - w1899291
 * @since 1.0
 */
public enum Cost {

    // £25 per hour for the existing patients
    COST_O(25.0),

    // £15 per hour for the new patients
    COST_N(15.0);

    private final double cost;

    // Constructor
    Cost(final double amount) {cost = amount;}

    // Implement getter
    public double getValue() {return cost;}
}
