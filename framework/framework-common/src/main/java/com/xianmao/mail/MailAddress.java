package com.xianmao.mail;

/**
 * @ClassName MailAddress
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-31 12:02
 * @Version 1.0
 */
public class MailAddress {

    private String name;
    private String address;

    public MailAddress(String address){
        this.address = address;
    }

    public MailAddress(String name,String address){
        this.name = name;
        this.address = address;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

}
