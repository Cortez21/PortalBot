package com.portalbot.main;

import org.junit.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ParserTest {
    @Test
    public void whenParsingTaskNumber() {
        Parser parser = new Parser();
        assertThat(parser.getTaskNumber(" rowspan=\"2\" style=\"background-color: yellow\" class=\"td_nocontext\">№ наряда:<br/>9151318</td>"),
                is("9151318"));
    }

    @Test
    public void whenParsingAccount() {
        Parser parser = new Parser();
        assertThat(parser.getAccount(" rowspan=\"2\" style=\"background-color: yellow\" class=\"td_nocontext\">ЛС:<br/><b>7335724</b></td>"),
                is("7335724"));
    }

    @Test
    public void whenParsingAddress() {
        Parser parser = new Parser();
        assertThat(parser.getAddress(" rowspan=\"2\"><a href=\"#\" addrid=\"42489\" class=\"viewAddrLink\">Хмельницький, вулиця Гайдара д.27/1, кв.98</a><br/><span class=\"small\">MDU_KHM00012</span></td>"),
                is("Хмельницький, вулиця Гайдара д.27/1, кв.98"));
    }

    @Test
    public void whenParsingName() {
        Parser parser = new Parser();
        assertThat(parser.getName(">Зельський Вадим Миколайович</td>"),
                is("Зельський Вадим Миколайович"));
    }
    @Test
    public void whenParsingServiceType() {
        Parser parser = new Parser();
        assertThat(parser.getType(">Сервис-Жалобы на обслуживание<br/>\n"
                        + "                                        <span title=\"Тип работы: FTTB FMC\">FTTB FMC</span></td>"),
                is("Сервис-Жалобы на обслуживание"));
    }

    @Test
    public void whenParsingNewType() {
        Parser parser = new Parser();
        assertThat(parser.getType(">Актив-Новое подключение</td>"),
                is("Актив-Новое подключение"));
    }

    @Test
    public void whenParsingBadStatus() {
        Parser parser = new Parser();
        assertThat(parser.getStatus(" rowspan=\"2\" style=\"background-color:red\">Отказ</td>\n"
                        + "            </tr>\n"
                        + "            <tr class=\"requestrow2\" rowid=\"558498\">"),
                is("Отказ"));
    }

    @Test
    public void whenParsingCompleteStatus() {
        Parser parser = new Parser();
        assertThat(parser.getStatus(" rowspan=\"2\" style=\"background-color:lightgreen\">Выполнено</td>\n"
                        + "            </tr>\n"
                        + "            <tr class=\"requestrow2\" rowid=\"558536\">"),
                is("Выполнено"));
    }

    @Test
    public void whenParsingPhoneNumber() {
        Parser parser = new Parser();
        assertThat(parser.getPhoneNumber(" rowspan=\"2\">380688769447,983733504<br/></td>"),
                is("380688769447,983733504"));
    }

    @Test
    public void whenParsingInstallationTuner() {
        Parser parser = new Parser();
        assertThat(parser.getInstall(">ТВ-тюнер:</td>", ">2"),
                is("ТВ-тюнер: 2"));

    }

    @Test
    public void whenParsingInstallationWithoutTuner() {
        Parser parser = new Parser();
        assertThat(parser.getInstall(">ТВ без тюнера:</td>", ">1"),
                is("ТВ без тюнера: 1"));

    }

    @Test
    public void whenParsingInstallationWithAlreadyExist() {
        Parser parser = new Parser();
        assertThat(parser.getInstall(">ТВ без тюнера:</td>", ">2", "ТВ-тюнер: 1"),
                is("ТВ-тюнер: 1, ТВ без тюнера: 2"));

    }

    @Test
    public void whenParsingTime() {
        Parser parser = new Parser();
        assertThat(parser.getTime("\"555860\" lastsbmstime=\"2019-04-11 20:22:26\" class=\"requestrow\">\n"
                        + "                <th rowspan=\"2\">1.</th>\n"
                        + "                <th rowspan=\"2\">10:00</th>\n"
                        + "                <th rowspan=\"2\">11:59</th>"),
                is("10:00"));

    }
}
