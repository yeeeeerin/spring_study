package com.example.vol2;

public class StringPrinter implements Printer {

    private StringBuffer buffer = new StringBuffer();

    /*
    내장 버퍼에 메세지를 추가한다.
     */
    @Override
    public void print(String message) {
        this.buffer.append(message);
    }

    /*
    내장 버퍼에 추가해둔 메세지를 스트링으로 가져온다.
     */
    public String toString(){
        return this.buffer.toString();
    }
}
