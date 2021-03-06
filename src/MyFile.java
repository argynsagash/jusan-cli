import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyFile {

    public static boolean flag = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        help();

        while (flag) {
            String commandLine = scanner.nextLine();
            String[] arr = commandLine.split(" ");

            if (arr[0].equals("exit")) exit();
            else if (arr[0].equals("ls")) listDirectory(arr[1]);
            else if (arr[0].equals("ls_py")) listPythonFiles(fileName(arr));
            else if (arr[0].equals("is_dir")) isDirectory(fileName(arr));
            else if (arr[0].equals("define")) define(fileName(arr));
            else if (arr[0].equals("readmod")) printPermissions(fileName(arr));
            else if (arr[0].equals("setmod")) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < arr.length - 2; i++) {
                    sb.append(arr[i] + " ");
                }
                sb.append(arr[arr.length - 2]);
                setPermissions(sb.toString(), arr[arr.length - 1]);
            } else if (arr[0].equals("cat")) printContent(fileName(arr));
            else if (arr[0].equals("append")) appendFooter(fileName(arr));
            else if (arr[0].equals("bc")) createBackup(fileName(arr));
            else if (arr[0].equals("greplong")) printLongestWord(fileName(arr));
            else if (arr[0].equals("help")) help();
            else System.out.println("Wrong command. Please try to write -> help for more options");
        }

    }

    // выводит список всех файлов и директорий для `path` - ls
    public static void listDirectory(String path) {
        try {
            File dir = new File(path); //path указывает на директорию
            for (File file : dir.listFiles()) {
                if (file.isFile())
                    System.out.println(file);
            }
        }catch (Exception e){
            System.out.println("Wrong Directory, try again");
        }

    }

    // выводит список файлов с расширением `.py` в `path` - ls_py
    public static void listPythonFiles(String path) {
        File dir = new File(path);
        File[] filesName = dir.listFiles();
        for (int i = 0; i < filesName.length; i++) {
            if (filesName[i].getName().contains(".py")) System.out.println(filesName[i].getName());
        }
    }

    // выводит `true`, если `path` это директория, в других случаях `false` - id_dir
    public static void isDirectory(String path) {
        File currentDir = new File(path);
        if(currentDir.isFile()) System.out.println(false);
        else if (currentDir.isDirectory()) System.out.println(true);
        else System.out.println("Error no such a directory");
    }

    // выводит `директория` или `файл` в зависимости от типа `path` - define
    public static void define(String path) {
        File currentDir = new File(path);
        if (currentDir.isFile()) System.out.println("файл");
        else if (currentDir.isDirectory()) System.out.println("директория");
        else System.out.println("Error: Wrong directory or file try again");
    }

    // выводит права для файла в формате `rwx` для текущего пользователя - readmod
    public static void printPermissions(String path) {

        File currentDir = new File(path);
        StringBuilder sb = new StringBuilder();
        if (currentDir.isFile()) {
            if (currentDir.canRead()) sb.append("r");
            else sb.append("-");
            if (currentDir.canWrite()) sb.append("w");
            else sb.append("-");
            if (currentDir.canExecute()) sb.append("x");
            else sb.append("-");
            System.out.println(sb);
        }
        else if (currentDir.isDirectory()) {
            if (currentDir.canRead()) sb.append("r");
            else sb.append("-");
            if (currentDir.canWrite()) sb.append("w");
            else sb.append("-");
            if (currentDir.canExecute()) sb.append("x");
            else sb.append("-");
            System.out.println(sb);
        }
        else System.out.println("Error: Wrong directory or file try again");
    }

    // устанавливает права для файла `path` - setmod
    public static void setPermissions(String path, String permissions) {
        File currentDir = new File(path);
        if (permissions.contains("r")) currentDir.setReadable(true);
        else currentDir.setReadable(false);
        if (permissions.contains("w")) currentDir.setWritable(true);
        else currentDir.setWritable(false);
        if (permissions.contains("x")) currentDir.setExecutable(true);
        else currentDir.setExecutable(false);
    }

    // выводит контент файла - cat
    public static void printContent(String path) {
        try {
            File file = new File(path);
            Scanner obj = new Scanner(file);
            while (obj.hasNextLine()) {
                System.out.println(obj.nextLine());
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    // добавляет строке `# Autogenerated line` в конец `path` - append
    public static void appendFooter(String path) {
        File file = new File(path);
        System.out.println(file.getAbsolutePath() + "# Autogenerated line");
    }

    // создает копию `path` в директорию `/tmp/${date}.backup` где, date - это дата в формате `dd-mm-yyyy`. `path`
    // может быть директорией или файлом. При директории, копируется весь контент. - bc
    public static void createBackup(String path) {
        GregorianCalendar curentTime = new GregorianCalendar();
        curentTime.getTimeZone();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(curentTime.getTime());
        File source = new File(path);
        File[] filesList = source.listFiles();
        if (source.isFile()) {
            File dest = new File(source.getParentFile() + "\\tmp" + "\\" + date + ".backup\\");
            dest.mkdirs();
            try {
                Files.copy(source.toPath(), Path.of(source.getParentFile() + "\\tmp" + "\\" + date + ".backup\\" + source.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (source.isDirectory()) {
            File dest = new File(source.getPath() + "\\tmp" + "\\" + date + ".backup\\");
            dest.mkdirs();
            for (int i = 0; i < filesList.length; i++) {
                try {
                    Files.copy(filesList[i].toPath(), Path.of(path + "\\tmp" + "\\" + date + ".backup\\" + filesList[i].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    // выводит самое длинное слово в файле - greplong
    public static void printLongestWord(String path) {
        try {
            File file = new File(path);
            Scanner obj = new Scanner(file);
            int maxLength = 0;
            while (obj.hasNextLine()) {
                String line = obj.nextLine();
                String[] substrings = line.split(" ");
                for (String s : substrings) {
                    if (s.length() > maxLength) maxLength = s.length();
                }
            }
            file = new File(path);
            obj = new Scanner(file);
            while (obj.hasNextLine()) {
                String line = obj.nextLine();
                String[] substrings = line.split(" ");
                for (String s : substrings) {
                    if (s.length() == maxLength) System.out.println(s);
                }
            }

        } catch (Exception e) {
            System.out.print(e);
        }
    }

    // выводит список команд и их описание - help
    public static void help() {
        System.out.println(
                "ls <path>               выводит список всех файлов и директорий для `path`\n" +
                        "ls_py <path>            выводит список файлов с расширением `.py` в `path`\n" +
                        "is_dir <path>           выводит `true`, если `path` это директория, в других случаях `false`\n" +
                        "define <path>           выводит `директория` или `файл` в зависимости от типа `path`\n" +
                        "readmod <path>          выводит права для файла в формате `rwx` для текущего пользователя\n" +
                        "setmod <path> <perm>    устанавливает права для файла `path`\n" +
                        "cat <path>              выводит контент файла\n" +
                        "append <path>           добавляет строку `# Autogenerated line` в конец `path`\n" +
                        "bc <path>               создает копию `path` в директорию `/tmp/${date}.backup` где, date - это дата в формате `dd-mm-yyyy`\n" +
                        "greplong <path>         выводит самое длинное слово в файле\n" +
                        "help                    выводит список команд и их описание\n" +
                        "exit                    завершает работу программы");
    }

    // завершает работу программы - exit
    public static void exit() {
        System.out.println("Goodbye");
        flag = false;
    }

    public static String fileName(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < arr.length - 1; i++) {
            sb.append(arr[i] + " ");
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }
}
