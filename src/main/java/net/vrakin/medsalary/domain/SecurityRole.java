package net.vrakin.medsalary.domain;

public enum SecurityRole {

    ADMIN, SMALL_ADMIN, USER;

    @Override
    public String toString() {
        return "ROLE_" + this.name();
    }
}
