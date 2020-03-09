package com.PerceptionHash;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
    PerceptionHash perceptionHash = new PerceptionHash();
    String image1 = "C:\\Users\\28175\\IdeaProjects\\人脸识别特征\\65174303e9f8fa0c8ead379becd3684b_ZXXKCOM201703030958514780931.jpg";
    String image2 = "C:\\Users\\28175\\IdeaProjects\\人脸识别特征\\65174303e9f8fa0c8ead379becd3684b_ZXXKCOM2017030309585147809131.jpg";
    int num = perceptionHash.Perceptionhash(image1,image2);
    System.out.println("相似度："+ num + "%");
    if(num > 60)
    {
        System.out.println("相似图片");
    }
    else
    {
        System.out.println("不同图片");
    }
    }
}
