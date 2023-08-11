package org.hust.entity.payment;


import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class represent a credit card and hold the information needed of a credit card.
 */
@Getter
@NoArgsConstructor
public class CreditCard {

    private String cardCode;
    private String owner;
    private String cvvCode;
    private String dateExpired;

    /**
     * Initialize a credit card object.
     *
     * @param cardCode    the credit card's code
     * @param owner       the credit card's owner
     * @param cvvCode     the credit card's security code
     * @param dateExpired the credit card's expired date
     */
    public CreditCard(String cardCode, String owner, String cvvCode, String dateExpired) {
        this.cardCode = cardCode;
        this.owner = owner;
        this.cvvCode = cvvCode;
        this.dateExpired = dateExpired;
    }

    /**
     * Return the owner of the credit card.
     *
     * @return the owner of the credit card
     */
    public String getOwner() {
        return this.owner;
    }
}
