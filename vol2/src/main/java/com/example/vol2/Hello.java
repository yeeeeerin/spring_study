package com.example.vol2;


public class Hello {

    String name;
    Printer printer;

    public String sayHello(){
        return "Hello " + name;
    }
    /*
    DI에 의해 의존 오브젝트로 제공받은 오브젝트에게 출력 작업을 위임한다.
    구체적으로 어떤 방식으로 출력하는지는 상관하지 않는다.
    또한 어떤 방식으로 출력하도록 변겨해도 코드는 수정할 필요가 없다.
     */
    public void print(){
        this.printer.print(sayHello());
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrinter(Printer printer){
        this.printer = printer;
    }

}
