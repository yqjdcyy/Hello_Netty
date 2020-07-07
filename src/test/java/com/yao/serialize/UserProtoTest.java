package com.yao.serialize;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Protobuf.User 的初始化和反序列化
 *
 * @author qingju.yao
 * @date 2020/07/07
 */
public class UserProtoTest {

    private UserProto.User user;

    @Before
    public void setUp() {

        UserProto.User.Builder builder = UserProto.User.newBuilder();
        builder.setId(1);
        builder.setName(this.getClass().getSimpleName());
        user = builder.build();
    }

    @Test
    public void decodeByByteArray() throws InvalidProtocolBufferException {

        UserProto.User userProto = UserProto.User.parseFrom(user.toByteArray());
        System.out.println(userProto);
    }

    @Test
    public void decodeByStream() throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        user.writeTo(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        UserProto.User user = UserProto.User.parseFrom(inputStream);
        System.out.println(user);
    }

    @Test
    public void decodeByDelimited() throws IOException {

        ByteOutputStream outputStream = new ByteOutputStream();
        user.writeDelimitedTo(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        UserProto.User user = UserProto.User.parseDelimitedFrom(inputStream);
        System.out.println(user);
    }
}
