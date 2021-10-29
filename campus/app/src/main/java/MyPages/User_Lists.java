package MyPages;

public class User_Lists {
    private String StuNo;
    private String Start;
    private String End;
    private String Reason;

    public User_Lists(String stuNo, String start, String end, String reason) {
        StuNo = stuNo;
        Start = start;
        End = end;
        Reason = reason;
    }

    public void setStuNo(String stuNo) {
        StuNo = stuNo;
    }

    public void setStart(String start) {
        Start = start;
    }

    public void setEnd(String end) {
        End = end;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getStuNo() {
        return StuNo;
    }

    public String getStart() {
        return Start;
    }

    public String getEnd() {
        return End;
    }

    public String getReason() {
        return Reason;
    }

    public String toString() {
        return   StuNo+" " +Start+" " +" "+End+" " +Reason;
    }

}
