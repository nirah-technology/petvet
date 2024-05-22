package io.nirahtech.petvet.core.contact;

import java.util.Optional;

public final class Contact {
    private Email email;
    private Mail mail;
    private PhoneNumber phoneNumber;

    public Contact() {

    }

    /**
     * @return the email
     */
    public Optional<Email> getEmail() {
        return Optional.ofNullable(email);
    }

    /**
     * @param email the email to set
     */
    public void setEmail(Email email) {
        this.email = email;
    }

    /**
     * @return the mail
     */
    public Optional<Mail> getMail() {
        return Optional.ofNullable(mail);
    }

    /**
     * @param mail the mail to set
     */
    public void setMail(Mail mail) {
        this.mail = mail;
    }

    /**
     * @return the phoneNumber
     */
    public Optional<PhoneNumber> getPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    
}
