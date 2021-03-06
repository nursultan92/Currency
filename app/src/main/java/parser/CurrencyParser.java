package parser;

import android.content.Context;
import android.os.AsyncTask;

import com.nurolopher.currency.MainActivity;
import com.nurolopher.currency.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

/**
 * Created by nursultan on 6-Feb 15.
 */
public class CurrencyParser extends AsyncTask<String, Integer, String[][]> {
    private MainActivity mainActivity;
    private Document document;
    private Elements rows;

    public CurrencyParser(Context context) {
        mainActivity = (MainActivity) context;
    }

    @Override
    protected String[][] doInBackground(String... urls) {
        if (Currency.currencyTable.length == 0) {
            Currency.currencyTable = new String[urls.length][4];
            int actual_index = 0;
            int count;
            for (int index = 0; index < urls.length; index++) {
                count = 0;
                try {
                    switch (urls[index]) {
                        case BankURL.ECO:
                            count = parseEco(actual_index, count);
                            break;
                        case BankURL.NBKR:
                            count = parseNBKR(actual_index, count);
                            break;
                        case BankURL.DEMIR:
                            count = parseDEMIR(actual_index, count);
                            break;
                        case BankURL.OPTIMA:
                            count = parseOPTIMA(actual_index, count);
                            break;
                        case BankURL.ROSIN:
                            count = parseROSIN(actual_index, count);
                            break;
                        case BankURL.KICB:
                            count = parseKICB(actual_index, count);
                            break;
                        case BankURL.BTA:
                            count = parseBTA(actual_index, count);
                            break;
                        case BankURL.AYIL:
                            count = parseAYIL(actual_index, count);
                            break;
                        case BankURL.RSK:
                            count = parseRSK(actual_index, count);
                            break;
                        case BankURL.CBK:
                            count = parseCBK(actual_index, count);
                            break;
                        case BankURL.FKB:
                            count = parseFKB(actual_index, count);
                            break;
                        case BankURL.DOS_CREDO:
                            count = parseDOS(actual_index, count);
                        default:
                            break;
                    }
                } catch (Exception ignored) {

                }
                actual_index += count;
                publishProgress(index);
            }

            Currency.normalizeCurrencyTable();
            return Currency.currencyTable;
        } else {
            Currency.normalizeCurrencyTable();
            return Currency.currencyTable;
        }

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.startProgressBar();
    }

    @Override
    protected void onPostExecute(String[][] currencies) {
        super.onPostExecute(currencies);

        Currency.currencyTable = currencies;
        mainActivity.progressDialog.cancel();

        mainActivity.showUpdateMessage();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mainActivity.toggleProgressBarMessage(values[0]);
        mainActivity.progressDialog.incrementProgressBy(1);

    }

    private int parseDEMIR(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.DEMIR).get();
            rows = document.select("#moneytable tr");
            for (int i = 1; i < 5; i++) {
                Elements rowCells = rows.get(i).select("td");
                if (rowCells.size() > 0) {
                    Currency.currencyTable[actual_index][i - 1] = mainActivity.getResources()
                            .getString(R.string.demir) +
                            ";" + rowCells.get(0).select("strong").get(0).text().trim() +
                            ";" + rowCells.get(1).text() + ";" + rowCells.get(2).text();
                    count = 1;
                }
            }
        } catch (Exception ignored) {

        }

        return count;
    }

    private int parseNBKR(int actual_index, int count) {
        try {
            document = Jsoup.parse(Jsoup.connect(BankURL.NBKR).get().body().toString(), BankURL.NBKR, Parser.xmlParser());
            rows = document.select("Currency");
            for (int i = 0; i < rows.size(); i++) {
                Currency.currencyTable[actual_index][i] = mainActivity.getResources().getString(R.string.nbkr) +
                        ";" + rows.get(i).attr("isocode").trim() +
                        ";" + rows.get(i).select("Value").get(0).text() +
                        ";" + rows.get(i).select("Value").get(0).text();
                count = 1;
            }
        } catch (Exception ignored) {

        }

        return count;
    }

    private int parseEco(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.ECO).get();
            rows = document.getElementsByClass("row");
            for (int i = 1; i < rows.size(); i++) {
                Elements rowCells = rows.get(i).getElementsByClass("cell");
                if (rowCells.size() > 0) {
                    Currency.currencyTable[actual_index][i - 1] = mainActivity.getResources().getString(R.string.eco) +
                            ";" + rowCells.get(0).text().trim() +
                            ";" + rowCells.get(1).text() +
                            ";" + rowCells.get(2).text();
                    count = 1;
                }
            }
        } catch (Exception ignored) {

        }

        return count;
    }

    private int parseOPTIMA(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.OPTIMA).get();
            Element table = document.select(".currency_table").first();
            Elements rows = table.select("tr");
            for (int i = 2; i < 6; i++) {
                Elements cell = rows.get(i).select("td span");
                Currency.currencyTable[actual_index][i - 2] = mainActivity.getResources()
                        .getString(R.string.optima) +
                        ";" + Currency.getCurrencyArrayAt(i - 2) +
                        ";" + cell.get(0).text() +
                        ";" + cell.get(1).text();
                count = 1;
            }
        } catch (Exception ignored) {

        }

        return count;
    }

    private int parseROSIN(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.ROSIN).get();
            Element table = document.getElementsByClass("currency").first().select("table").first();
            Elements rows = table.select("tbody tr");
            for (int i = 0; i < 4; i++) {
                Elements cells = rows.get(i).select("td");
                Currency.currencyTable[actual_index][i] = mainActivity.getResources().
                        getString(R.string.rosin) +
                        ";" + cells.get(0).select("div").first().text().trim() +
                        ";" + cells.get(1).text() +
                        ";" + cells.get(2).text();
                count = 1;
            }
        } catch (Exception ignored) {

        }

        return count;
    }

    private int parseKICB(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.KICB).get();
            Element table = document.getElementsByClass("con").first();
            Elements rows = table.getElementsByClass("cur_line");
            rows.remove(rows.select("img"));
            for (int i = 1; i < 5; i++) {
                Elements cells = rows.get(i).select("span");
                Currency.currencyTable[actual_index][i - 1] = mainActivity.getResources().
                        getString(R.string.kicb) +
                        ";" + cells.get(0).text().trim() +
                        ";" + cells.get(1).text() +
                        ";" + cells.get(2).text();
                count = 1;
            }
        } catch (Exception ignored) {

        }

        return count;
    }

    private int parseBTA(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.BTA).get();
            Elements rows = document.select("table.currency tbody tr");
            for (int i = 2; i < 6; i++) {
                Elements cells = rows.get(i).select("td");
                Currency.currencyTable[actual_index][i - 2] = mainActivity.getResources().
                        getString(R.string.bta) +
                        ";" + cells.get(0).text().trim() +
                        ";" + cells.get(1).text() +
                        ";" + cells.get(2).text();
                count = 1;
            }
        } catch (Exception ignored) {

        }

        return count;
    }

    private int parseAYIL(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.AYIL).get();
            Elements rows = document.select("#ja-col1 table tbody tr");
            for (int i = 1; i < rows.size(); i++) {
                Elements cells = rows.get(i).select("td");
                if (i == 4)
                    Currency.currencyTable[actual_index][i - 1] = mainActivity.getResources().
                            getString(R.string.ayil) +
                            ";USD;" + cells.get(1).text() +
                            ";" + cells.get(2).text();
                else
                    Currency.currencyTable[actual_index][i - 1] = mainActivity.getResources().
                            getString(R.string.ayil) +
                            ";" + cells.get(0).text() +
                            ";" + cells.get(1).text() +
                            ";" + cells.get(2).text();
                count = 1;
            }
        } catch (Exception ignored) {

        }

        return count;
    }

    private int parseRSK(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.RSK).get();
            Element table = document.select(".course-list").first();
            Elements rows = table.select(".item");
            for (int i = 0; i < 4; i++) {
                Elements cells = rows.get(i).select("div");
                Currency.currencyTable[actual_index][i] = mainActivity.getResources().
                        getString(R.string.rsk) +
                        ";" + cells.get(1).text() +
                        ";" + cells.get(2).text() +
                        ";" + cells.get(3).text();
                count = 1;
            }
        } catch (Exception ignored) {

        }
        return count;
    }

    private int parseCBK(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.CBK).get();
            Element table = document.select("#rates-table .table tbody").first();

            //USD
            Element cellUSD = table.select(".usd").first();
            Currency.currencyTable[actual_index][0] = mainActivity.getResources()
                    .getString(R.string.cbk) +
                    ";" + Currency.USD +
                    ";" + cellUSD.select(".buy").text() +
                    ";" + cellUSD.select(".sell").text();

            //EUR
            Element cellEUR = table.select(".euro").first();
            Currency.currencyTable[actual_index][1] = mainActivity.getResources().
                    getString(R.string.cbk) +
                    ";" + Currency.EUR +
                    ";" + cellEUR.select(".buy").text() +
                    ";" + cellEUR.select(".sell").text();

            //RUB
            Element cellRUB = table.select(".rub").first();
            Currency.currencyTable[actual_index][2] = mainActivity.getResources()
                    .getString(R.string.cbk) +
                    ";" + Currency.RUB +
                    ";" + cellRUB.select(".buy").text() +
                    ";" + cellRUB.select(".sell").text();

            //KZT
            Element cellKZT = table.select(".kzt").first();
            Currency.currencyTable[actual_index][3] = mainActivity.getResources()
                    .getString(R.string.cbk) +
                    ";" + Currency.KZT +
                    ";" + cellKZT.select(".buy").text() +
                    ";" + cellKZT.select(".sell").text();
            count = 1;

        } catch (Exception ignored) {

        }
        return count;
    }

    private int parseFKB(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.FKB).get();
            Elements rows = document.select(".curency tbody tr");
            rows.removeAll(rows.select("img"));
            for (int i = 1; i < 5; i++) {
                Elements cells = rows.get(i).select("td");
                Currency.currencyTable[actual_index][i - 1] = mainActivity.getResources()
                        .getString(R.string.fkb) +
                        ";" + cells.get(0).select("span").first().text() +
                        ";" + cells.get(1).text().trim() +
                        ";" + cells.get(2).text().trim();
                count = 1;
            }
        } catch (Exception ignored) {

        }

        return count;
    }

    private int parseDOS(int actual_index, int count) {
        try {
            document = Jsoup.connect(BankURL.DOS_CREDO).get();
            Element table = document.select(".b-currency-rates").first();
            Elements rows = table.select("tbody tr");
            for (int i = 0; i < 4; i++) {
                String currency = rows.get(i).select("th").first().text();
                Elements cells = rows.get(i).select("td");
                String buy = cells.get(0).text();
                String sell = cells.get(1).text();
                Currency.currencyTable[actual_index][i] = mainActivity.getResources()
                        .getString(R.string.dos_credo) +
                        ";" + currency + ";" + buy + ";" + sell;
                count = 1;
            }

        } catch (Exception ignored) {

        }
        return count;
    }
}
