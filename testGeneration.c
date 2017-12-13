#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char const *argv[])
{

    if (argc == 1 || strncmp(argv[1], "-h", 2) == 0){
        fprintf(stdout, "%s <test number> <key value list>\n", argv[0]);
        return 1;
    }
    else{
        FILE *file ;
        char* currentString = (char *) malloc(1024 * sizeof(char));
        char *filename = (char *) malloc(1024 * sizeof(char));
        strcat(filename, "src/options/");
        strcat(filename, argv[1]); 
        file = fopen(filename, "w+");

        // Add xml header 
        strcat(currentString, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        strcat(currentString, "\n");
        strcat(currentString, "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\" >\n");
        strcat(currentString, "<properties>\n");
        strcat(currentString, "\t<comment>\n");
        strcat(currentString, "\t\tCette configuration ...\n");
        strcat(currentString, "\t</comment>\n");

        // foreach (theKey, theValue) of the list
        // add "    <entry key="theValue">theValue</entry>"
        for (int i = 2; i < argc; i += 2){
            printf("%s : %s\n", argv[i], argv[i + 1]);
            strcat(currentString, "\t<entry key=\"");
            strcat(currentString, argv[i]);
            strcat(currentString, "\">");
            strcat(currentString, argv[i + 1]);
            strcat(currentString, "</entry>\n");
        }
        strcat(currentString, "</properties>%");
        fprintf(file, "%s\n", currentString);
        fclose(file);
        return 0;
    }
}