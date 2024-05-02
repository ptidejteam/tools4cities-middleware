package com.middleware;
import py4j.GatewayServer;

public class StackEntryPoint {

    private Stack stack;

    public StackEntryPoint(){
        stack = new Stack();
    }

    public Stack getStack(){
        return stack;
    }

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new StackEntryPoint());
        gatewayServer.start();
        System.out.println("Gateway Server Started...");
    }
}
ALLOWED_ORIGINS=https://buyer-admin-test-amp.completefarmer.com,https://apis-shf-test.completefarmer.com,https://apis-shf.completefarmer.com,https://admin-test.completefarmer.com,https://admin-demo.completefarmer.com,https://admin.completefarmer.com,http://localhost:1738,http://localhost:7005,http://localhost:3002,http://localhost:5000,http://localhost:5008,http://localhost:5009,http://localhost:5010,http://localhost:3000,http://localhost:3003,http://localhost:5001,http://localhost:5000,https://digitalfarmer.completefarmer.com,https://digitalfarmer-test.completefarmer.com,https://operations.completefarmer.com,https://operations-test.completefarmer.com,https://buyer.completefarmer.com,https://buyer-test.completefarmer.com,https://grower.completefarmer.com,https://grower-test.completefarmer.com,https://api.paystack.co/transaction,https://completefarmer.com,https://www.completefarmer.com,https://completefarmer-test.netlify.app,https://digitalfarmer-demo.completefarmer.com,https://operations-demo.completefarmer.com,https://buyer-demo.completefarmer.com,https://grower-demo.completefarmer.com,https://cf-website-v4-eight.vercel.app,https://auth-test.completefarmer.com,https://auth-demo.completefarmer.com,https://auth.completefarmer.com,https://farm-mgmt-test.completefarmer.com,https://farm-mgmt-demo.completefarmer.com,https://farm-mgmt.completefarmer.com,https://buyer-admin-test.completefarmer.com,https://buyer-admin-demo.completefarmer.com,https://buyer-admin.completefarmer.com,https://test.completefarmer.com,https://demo.completefarmer.com,https://buyer-dasboard-test.completefarmer.com,https://vendor-test.completefarmer.com,https://vendor.completefarmer.com