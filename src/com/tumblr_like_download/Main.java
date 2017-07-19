package com.tumblr_like_download;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.User;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;

import java.net.URL;
import java.io.File;
import java.io.IOException;

public class Main {
    static String username;
    static String pwd;
    static int userLikeCount;
    static int offset;

    public static void main(String[] args) {

        JumblrClient client = new JumblrClient(
                "sVwgEbcH5p9QpVaMLjpvXdfryYXU5AY5R5lH3qYVQaXzy7LYQi",
                "n1M4E1VOgO5npc3j6aZx4HiP4vtO5t0LbOwHEisNSg4QBv8Pbq"
        );
        client.setToken(
                "eWhLh3mSvWt5c7zyBzuVo5gx7RVVFLcvXeua0Gkovyd9DLY8i8",
                "fW4dOlJdSO6ST54VMLI72t5uqmxZcxSAc6sXDNwjdbVb1t6YGq"
        );

        // Make the request
        User user = client.user();
        userLikeCount = user.getLikeCount();
        offset = 1;
        List<Blog> blogs = user.getBlogs();
        List<URL> url_list = new ArrayList<>();
 
        while (offset < userLikeCount) {

            Map<String, Integer> options = new HashMap<>();
            options.put("limit", 5);
            options.put("offset", offset + 1);
            List<Post> posts = client.userLikes(options);

            for (Post img_url : posts) {
                try {
                    Document doc = Jsoup.connect(img_url.getPostUrl()).get();
                    Elements img = doc.select("img[src]");
                    int length = img.size();

                    for (int i = 0; i < length; i++) {
                        if (!img.get(i).absUrl("src").contains("avatar") && img.get(i).absUrl("src").contains(".jpg") && !img.get(i).absUrl("src").contains("250")) {
                            URL url = new URL(img.get(i).absUrl("src"));
                            if (!url_list.contains(url)) {
                                url_list.add(url);
                                Random rand = new Random();
                                System.out.println("downloading.. " + url.toString() + " to file " + "/Users/torrybrelsford/Documents/Photos/Look-Book/test/");
                                FileUtils.copyURLToFile(url, new File("/Users/torrybrelsford/Documents/Photos/Look-Book/test/", rand.nextInt(25000) + ".jpg"));
                            }

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            System.out.println(offset);
            offset += 5;
        }


    }
}
