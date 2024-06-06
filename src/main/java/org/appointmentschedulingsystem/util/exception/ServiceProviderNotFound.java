package org.appointmentschedulingsystem.util.exception;

public class ServiceProviderNotFound extends RuntimeException{
    public ServiceProviderNotFound(String msg) {
        super(msg);
    }
}
