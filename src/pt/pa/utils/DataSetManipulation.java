package pt.pa.utils;

import pt.pa.graph.Graph;
import pt.pa.graph.Vertex;
import pt.pa.model.Coordinate;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class that manipulates dataset files.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class DataSetManipulation {
    /*
     *
     * Escolher a pasta sgb
     * Verificar se existem os ficheiros name.txt | weight.txt e xy.txt existem
     * Verificar quantos ficheiros de routes.txt existem
     *
     * para a implementação de rotas, criar um mapa (Inteiro, Inteiro) que associa a linha e coluna que estão ligadas (para evitar rotas iguais)
     *
     */
    private List<String> dataSetFileList;
    private String directory;

    public DataSetManipulation(String directory) {
        this.dataSetFileList = new ArrayList<>();
        this.directory = directory;

        File folder = new File("dataset/" + directory + "/");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                this.dataSetFileList.add(listOfFiles[i].getName());
            }
        }
    }

    public int getNumberOfHubs() throws FileNotFoundException {
        int lineCount = 0;
        String fileName = getFileNameFromList("name.txt");
        File countLines = new File("dataset/" + directory + "/" + fileName);
        Scanner myReader = new Scanner(countLines);
        while (myReader.hasNextLine()) {
            myReader.nextLine();
            lineCount++;
        }
        myReader.close();
        return lineCount;
    }

    public void insertHubsFromDirectory(Graph<Hub, Route> graph) throws FileNotFoundException, NumberFormatException {

        int numberOfHubs = getNumberOfHubs();

        String[] cityName = new String[numberOfHubs];
        int[] population = new int[numberOfHubs];
        Coordinate[] coordinates = new Coordinate[numberOfHubs];

        String fileName = getFileNameFromList("name.txt");

        File fileCity = new File("dataset/" + directory + "/" + fileName);
        Scanner myReader = new Scanner(fileCity);
        String nextLine;
        int count = 0;
        while (myReader.hasNextLine()) {
            nextLine = myReader.nextLine();
            if (!nextLine.isEmpty() || nextLine != null) {
                String[] data = nextLine.split(", ");
                cityName[count] = nextLine;
                count++;
            }
        }
        myReader.close();

        fileName = getFileNameFromList("weight.txt");

        File filePopulation = new File("dataset/" + directory + "/" + fileName);
        myReader = new Scanner(filePopulation);
        count = 0;
        while (myReader.hasNextLine()) {
            nextLine = myReader.nextLine();
            if (!nextLine.isEmpty() || nextLine != null) {
                population[count] = Integer.valueOf(nextLine);
                count++;
            }
        }
        myReader.close();

        fileName = getFileNameFromList("xy.txt");

        File fileCoordinate = new File("dataset/" + directory + "/" + fileName);
        myReader = new Scanner(fileCoordinate);
        count = 0;
        while (myReader.hasNextLine()) {
            nextLine = myReader.nextLine();
            if (!nextLine.isEmpty() || nextLine != null) {
                String[] data = nextLine.split(" ");
                coordinates[count] = new Coordinate(Integer.valueOf(data[0]), Integer.valueOf(data[1]));
                count++;
            }
        }
        myReader.close();
        for (int i = 0; i < numberOfHubs; i++) {
            Hub hubTemp = new Hub(cityName[i], population[i], coordinates[i]);
            graph.insertVertex(hubTemp);
        }
    }

    public void insertRoutesFromDirectory(Graph<Hub, Route> graph) throws FileNotFoundException, NumberFormatException {
        int numberOfHubs = getNumberOfHubs();
        int row = 0;

        List<String> routesList = new ArrayList<>();

        for (String fileName : dataSetFileList) {
            String[] data = fileName.split("_");
            if (data[0].equals("routes")) {
                routesList.add(fileName);
            }
        }

        for (int i = 0; i < routesList.size(); i++) {
            String fileName = routesList.get(i);

            File fileRoutes = new File("dataset/" + directory + "/" + fileName);

            Scanner myReader = new Scanner(fileRoutes);
            Collection<Vertex<Hub>> hubs = graph.vertices();
            while (myReader.hasNextLine()) {
                String nextLine = myReader.nextLine();
                if (!nextLine.isEmpty()) {
                    String[] data = nextLine.split(" ");
                    if (data.length == getNumberOfHubs()) {
                        for (int j = 0; j < data.length; j++) {
                            int part = Integer.parseInt(data[j]);
                            if (part > 0) {

                                Route route = new Route(part);
                                Hub Hub1 = null;
                                Hub Hub2 = null;
                                Vertex<Hub> vHub1 = null;
                                Vertex<Hub> vHub2 = null;

                                for (Vertex<Hub> vHub : hubs) {
                                    Hub hubTemp = vHub.element();
                                    if (row != j) {
                                        if (hubTemp.getIdentifier() - 1 == row) {
                                            Hub1 = hubTemp;
                                            vHub1 = vHub;
                                        } else if (hubTemp.getIdentifier() - 1 == j) {
                                            Hub2 = hubTemp;
                                            vHub2 = vHub;
                                        }
                                    }
                                }
                                if (!graph.areAdjacent(vHub1, vHub2)) {
                                    if (Hub1 != null && Hub2 != null)
                                        graph.insertEdge(Hub1, Hub2, route);
                                }

                            }
                        }
                        row++;
                    }
                }
            }
            row = 0;
        }


    }

    private String getFileNameFromList(String name) {
        for (String nameFile : this.dataSetFileList) {
            if (nameFile.equals(name)) {
                return nameFile;
            }
        }
        return null;
    }
}
