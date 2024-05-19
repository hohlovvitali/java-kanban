package manager.managerexception;

import tasks.Task;

public class ManagerTaskNotFoundException extends Exception{
    public ManagerTaskNotFoundException(String message){
        super(message);
    }
}
