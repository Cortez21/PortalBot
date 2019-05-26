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
    @Test
    public void whenParsingTheNewTaskWithThreeInstallation() {
        Parser parser = new Parser();
        Task expect = new Task("9122082");
        Task sourceTask;
        expect.setTime("10:00");
        expect.setAccount("8928976");
        expect.setAddress("Хмельницький, вулиця Чорновола д.60, кв.16");
        expect.setName("Дробян Вахтанг Альбертович");
        expect.setTaskType("Пассив-Новое подключение");
        expect.setInstallation("Роутер: 1, ТВ-тюнер: 1, ТВ без тюнера: 2");
        expect.setStatus("Выполнено");
        expect.setPhoneNumber("0962103500");
        String source = "\"555860\" lastsbmstime=\"2019-04-11 20:22:26\" class=\"requestrow\">\n"
                + "                <th rowspan=\"2\">1.</th>\n"
                + "                <th rowspan=\"2\">10:00</th>\n"
                + "                <th rowspan=\"2\">11:59</th>\n"
                + "                <td rowspan=\"2\">№заявки:<br/><a href=\"#\" onclick=\"editRequest(555860)\">555860</a><br/>\n"
                + "                    <a href=\"#\" class=\"viewRQLink\" rqid=\"555860\"><span class=\"glyphicon glyphicon-info-sign info\"></span></a>\n"
                + "                </td>\n"
                + "\n"
                + "                <td rowspan=\"2\" style=\"background-color: yellow\" class=\"td_nocontext\">№ наряда:<br/>9122082</td>\n"
                + "                <td rowspan=\"2\" style=\"background-color: yellow\" class=\"td_nocontext\">ЛС:<br/><b>8928976</b></td>\n"
                + "                <td rowspan=\"2\"><a href=\"#\" addrid=\"42485\" class=\"viewAddrLink\">Хмельницький, вулиця Чорновола д.60, кв.16</a><br/><span class=\"small\">MDU_KHM00012</span></td>\n"
                + "                <td>Дробян Вахтанг Альбертович</td>\n"
                + "                <td>Пассив-Новое подключение</td>\n"
                + "                <td rowspan=\"2\">\n"
                + "                    <table>\n"
                + "                                                    <tr><td>Роутер:</td><td>1</td></tr>\n"
                + "                                                    <tr><td>ТВ-тюнер:</td><td>1</td></tr>\n"
                + "                                                    <tr><td>ТВ без тюнера:</td><td>2</td></tr>\n"
                + "                                            </table>\n"
                + "                </td>\n"
                + "                <td class=\"networkstatus \" colspan=\"2\">Выполнена</td>\n"
                + "                <td rowspan=\"2\" style=\"background-color:lightgreen\">Выполнено</td>\n"
                + "            </tr>\n"
                + "            <tr class=\"requestrow2\" rowid=\"555860\"><td rowspan=\"2\">0962103500<br/></td>\n"
                + "                <td class=\"lastsbmstime\" title=\"Время последнего обновления от заказчика\">2019-04-11 20:22:26</td>\n"
                + "                <td class=\"sbmsduration\" title=\"Таймер заказчика\">26:30:53</td>\n"
                + "                <td class=\"\" title=\"Время подтверждения\"></td>\n"
                + "            </tr>\n"
                + "                                ";
        sourceTask = parser.taskParser(source);
        sourceTask.setDate("2019-04-11");
        expect.setDate("2019-04-11");
        assertThat(sourceTask.toMessage(), is(expect.toMessage()));
    }

    @Test
    public void whenParsingServiceWithoutInstallations() {
        Parser parser = new Parser();
        Task expect = new Task("9123056");
        Task sourceTask;
        expect.setTime("14:00");
        expect.setAccount("8318041");
        expect.setAddress("Хмельницький,   Ціолковського д.5/1, кв.74");
        expect.setName("Ратушна Світлана Борисівна");
        expect.setTaskType("Сервис-Жалобы на обслуживание");
        expect.setStatus("Выполнено");
        expect.setPhoneNumber("380964502937,964502937");
        String source = "\"555946\" lastsbmstime=\"2019-04-12 23:20:08\" class=\"requestrow\">\n"
                + "                <th rowspan=\"2\">2.</th>\n"
                + "                <th rowspan=\"2\">14:00</th>\n"
                + "                <th rowspan=\"2\">14:59</th>\n"
                + "                <td rowspan=\"2\">№заявки:<br/><a href=\"#\" onclick=\"editRequest(555946)\">555946</a><br/>\n"
                + "                    <a href=\"#\" class=\"viewRQLink\" rqid=\"555946\"><span class=\"glyphicon glyphicon-info-sign info\"></span></a>\n"
                + "                </td>\n"
                + "\n"
                + "                <td rowspan=\"2\" style=\"background-color: yellow\" class=\"td_nocontext\">№ наряда:<br/>9123056</td>\n"
                + "                <td rowspan=\"2\" style=\"background-color: yellow\" class=\"td_nocontext\">ЛС:<br/><b>8318041</b></td>\n"
                + "                <td rowspan=\"2\"><a href=\"#\" addrid=\"22719410\" class=\"viewAddrLink\">Хмельницький,   Ціолковського д.5/1, кв.74</a><br/><span class=\"small\">MDU_KHM00003</span></td>\n"
                + "                <td>Ратушна Світлана Борисівна</td>\n"
                + "                <td>Сервис-Жалобы на обслуживание<br/>\n"
                + "                                        <span title=\"Тип работы: FTTB FMC\">FTTB FMC</span></td>\n"
                + "                <td rowspan=\"2\">\n"
                + "                    <table>\n"
                + "                                            </table>\n"
                + "                </td>\n"
                + "                <td class=\"networkstatus \" colspan=\"2\">Выполнена</td>\n"
                + "                <td rowspan=\"2\" style=\"background-color:lightgreen\">Выполнено</td>\n"
                + "            </tr>\n"
                + "            <tr class=\"requestrow2\" rowid=\"555946\"><td rowspan=\"2\">380964502937,964502937<br/></td>\n"
                + "                <td class=\"lastsbmstime\" title=\"Время последнего обновления от заказчика\">2019-04-12 23:20:08</td>\n"
                + "                <td class=\"sbmsduration\" title=\"Таймер заказчика\">24:05:17</td>\n"
                + "                <td class=\"\" title=\"Время подтверждения\"></td>\n"
                + "            </tr>\n"
                + "                                ";
        sourceTask = parser.taskParser(source);
        sourceTask.setDate("2019-04-11");
        expect.setDate("2019-04-11");
        assertThat(sourceTask.toMessage(), is(expect.toMessage()));
    }
}
