package com.portalbot.main;

import org.junit.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TaskParserTest {
    @Test
    public void whenParsingTaskNumber() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getTaskNumber(" rowspan=\"2\" style=\"background-color: yellow\" class=\"td_nocontext\">№ наряда:<br/>9151318</td>"),
                is("9151318"));
    }

    @Test
    public void whenParsingAccount() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getAccount(" rowspan=\"2\" style=\"background-color: yellow\" class=\"td_nocontext\">ЛС:<br/><b>7335724</b></td>"),
                is("7335724"));
    }

    @Test
    public void whenParsingAddress() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getAddress(" rowspan=\"2\"><a href=\"#\" addrid=\"42489\" class=\"viewAddrLink\">Хмельницький, вулиця Гайдара д.27/1, кв.98</a><br/><span class=\"small\">MDU_KHM00012</span></td>"),
                is("Хмельницький, вулиця Гайдара д.27/1, кв.98"));
    }

    @Test
    public void whenParsingName() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getName(">Зельський Вадим Миколайович</td>"),
                is("Зельський Вадим Миколайович"));
    }
    @Test
    public void whenParsingServiceType() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getType(">Сервис-Жалобы на обслуживание<br/>\n"
                        + "                                        <span title=\"Тип работы: FTTB FMC\">FTTB FMC</span></td>"),
                is("Сервис-Жалобы на обслуживание"));
    }

    @Test
    public void whenParsingNewType() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getType(">Актив-Новое подключение</td>"),
                is("Актив-Новое подключение"));
    }

    @Test
    public void whenParsingBadStatus() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getStatus(" rowspan=\"2\" style=\"background-color:red\">Отказ</td>\n"
                        + "            </tr>\n"
                        + "            <tr class=\"requestrow2\" rowid=\"558498\">"),
                is("Отказ"));
    }

    @Test
    public void whenParsingCompleteStatus() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getStatus(" rowspan=\"2\" style=\"background-color:lightgreen\">Выполнено</td>\n"
                        + "            </tr>\n"
                        + "            <tr class=\"requestrow2\" rowid=\"558536\">"),
                is("Выполнено"));
    }

    @Test
    public void whenParsingPhoneNumber() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getPhoneNumber(" rowspan=\"2\">380688769447,983733504<br/></td>"),
                is("380688769447,983733504"));
    }

    @Test
    public void whenParsingInstallationTuner() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getInstall(">ТВ-тюнер:</td>", ">2"),
                is("ТВ-тюнер: 2"));

    }

    @Test
    public void whenParsingInstallationWithoutTuner() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getInstall(">ТВ без тюнера:</td>", ">1"),
                is("ТВ без тюнера: 1"));

    }

    @Test
    public void whenParsingInstallationWithAlreadyExist() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getInstall(">ТВ без тюнера:</td>", ">2", "ТВ-тюнер: 1"),
                is("ТВ-тюнер: 1, ТВ без тюнера: 2"));

    }

    @Test
    public void whenParsingTime() {
        TaskParser taskParser = new TaskParser();
        assertThat(taskParser.getTime("\"555860\" lastsbmstime=\"2019-04-11 20:22:26\" class=\"requestrow\">\n"
                        + "                <th rowspan=\"2\">1.</th>\n"
                        + "                <th rowspan=\"2\">10:00</th>\n"
                        + "                <th rowspan=\"2\">11:59</th>"),
                is("10:00"));

    }
}
