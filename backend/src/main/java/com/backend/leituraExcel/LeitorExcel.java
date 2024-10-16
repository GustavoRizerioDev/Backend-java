package main.java.com.backend.leituraExcel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LeitorExcel {

    public List<Energia> extrairEnergia(String nomeArquivo, InputStream arquivo) {
        List<Energia> energiaExtraida = new ArrayList<>();

        try (Workbook workbook = nomeArquivo.endsWith(".xlsx") ? new XSSFWorkbook(arquivo) : new HSSFWorkbook(arquivo)) {
            Sheet sheet = workbook.getSheetAt(0);
            String[] meses = {"janeiro", "fevereiro", "março", "abril", "maio", "junho",
                    "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};

            lerKwh(sheet, energiaExtraida, meses);
            lerGastos(sheet, energiaExtraida, meses);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage(), e);
        }

        return energiaExtraida;
    }

    private void lerKwh(Sheet sheet, List<Energia> energiaExtraida, String[] meses) {
        for (Row row : sheet) {
            if (row.getRowNum() < 6 || row.getRowNum() > 16) continue;

            String local = lerLocal(row);
            if (local == null) {
                System.out.println("Local ignorado na leitura de kWh.");
                continue;
            }

            System.out.println("Processando kWh para: " + local);

            Double[] kwh = new Double[12];
            for (int i = 0; i < 12; i++) {
                kwh[i] = lerValorCelula(row.getCell(3 + i));
            }

            for (int i = 0; i < 12; i++) {
                if (kwh[i] != null && kwh[i] > 0) {
                    Energia energia = new Energia();
                    energia.setLocal(local);
                    energia.setMes(meses[i]);
                    energia.setKwh(kwh[i]);
                    energia.setGasto(0.0);
                    energia.setAno(2024);
                    energiaExtraida.add(energia);
                    System.out.println("kWh adicionado: " + energia);
                }
            }
        }
    }

    private void lerGastos(Sheet sheet, List<Energia> energiaExtraida, String[] meses) {
        for (Row row : sheet) {
            if (row.getRowNum() < 21 || row.getRowNum() > 31) continue;

            String local = lerLocal(row);
            if (local == null) continue;

            System.out.println("Processando gastos para: " + local);

            Double[] gastos = new Double[12];
            for (int i = 0; i < 12; i++) {
                gastos[i] = lerValorCelula(row.getCell(3 + i));

                System.out.printf("Gasto lido para %s em %s: %.2f%n", local, meses[i], gastos[i]);
            }

            for (int i = 0; i < 12; i++) {
                for (Energia energia : energiaExtraida) {
                    if (energia.getLocal().trim().equalsIgnoreCase(local.trim()) && energia.getMes().equals(meses[i])) {
                        System.out.printf("Comparando: %s e %s em %s. Gasto lido: %.2f%n", energia.getLocal(), local, meses[i], gastos[i]);

                        if (gastos[i] != null && gastos[i] > 0) {
                            energia.setGasto(gastos[i]);
                            System.out.printf("Gasto atualizado para: %s em %s: %.2f%n", energia.getLocal(), energia.getMes(), gastos[i]);
                        } else {
                            System.out.printf("Gasto não atualizado para: %s em %s, valor: %.2f (deve ser maior que zero)%n", energia.getLocal(), energia.getMes(), gastos[i]);
                        }
                    }
                }
            }
        }
    }



    private String lerLocal(Row row) {
        Cell localCell = row.getCell(0);
        if (localCell == null || localCell.getCellType() != CellType.STRING) {
            return null;
        }
        String local = localCell.getStringCellValue();
        if (local.equalsIgnoreCase("Total") || local.equalsIgnoreCase("Média")) {
            return null;
        }
        return local;
    }

    private Double lerValorCelula(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }
        return 0.0;
    }

}
