package org.techtown.slowletter;

public class Inbox_Item {
    //보낸날짜
    int s_year;
    int s_month;
    int s_day;

    //받을 날짜
    int r_year;
    int r_month;
    int r_day;

    //편지를 열지 말지 (기준 : d-day)
    boolean letter_open;

    public boolean isLetter_open() {
        return letter_open;
    }

    public void setLetter_open(boolean letter_open) {
        this.letter_open = letter_open;
    }

    public Inbox_Item(int s_year, int s_month, int s_day, int r_year, int r_month, int r_day){
        this.s_year=s_year;
        this.s_month=s_month;
        this.s_day=s_day;
        this.r_year=r_year;
        this.r_month=r_month;
        this.r_day=r_day;
    }

    public int getS_year() {
        return s_year;
    }

    public void setS_year(int s_year) {
        this.s_year = s_year;
    }

    public int getS_month() {
        return s_month;
    }

    public void setS_month(int s_month) {
        this.s_month = s_month;
    }

    public int getS_day() {
        return s_day;
    }

    public void setS_day(int s_day) {
        this.s_day = s_day;
    }

    public int getR_year() {
        return r_year;
    }

    public void setR_year(int r_year) {
        this.r_year = r_year;
    }

    public int getR_month() {
        return r_month;
    }

    public void setR_month(int r_month) {
        this.r_month = r_month;
    }

    public int getR_day() {
        return r_day;
    }

    public void setR_day(int r_day) {
        this.r_day = r_day;
    }
}
