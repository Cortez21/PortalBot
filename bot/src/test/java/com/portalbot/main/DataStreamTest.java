package com.portalbot.main;


import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DataStreamTest {
    @Test
    public void whenAddingPortal3030AndSecurityCodeIsAbsent() {
        DataStream data = new DataStream();
        String entryPortal3030 = "12345";
        data.setPortal3030(entryPortal3030);
        assertThat(data.getProperties().containsValue(String.format("portal3030=%s", entryPortal3030)), is(true));
    }

    @Test
    public void whenAddingPortal3030AndSecurityCodeExists() {
        DataStream data = new DataStream();
        String entryCode = "54321";
        String entryPortal = "12345";
        data.setSecurityCode(entryCode);
        data.setPortal3030(entryPortal);
        assertThat(data.getProperties().containsValue(String.format("portal3030=%s; sessioncode=%s", entryPortal, entryCode)), is(true));
    }

    @Test
    public void whenAddingSecurityCodeAndPortal3030IsAbsent() {
        DataStream data = new DataStream();
        String entryCode = "12345";
        data.setSecurityCode(entryCode);
        assertThat(data.getProperties().containsValue(String.format("sessioncode=%s", entryCode)), is(true));
    }

    @Test
    public void whenAddingSecurityCodeAndPortal3030Exists() {
        DataStream data = new DataStream();
        String entryCode = "12345";
        String entryPortal3030 = "54321";
        data.setSecurityCode(entryCode);
        data.setPortal3030(entryPortal3030);
        assertThat(data.getProperties().containsValue(String.format("portal3030=%s; sessioncode=%s", entryPortal3030, entryCode)), is(true));
    }
}
