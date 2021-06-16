package az.storage;


import az.storage.services.AzureStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AzureBLOBArchivalJob {

    private  final AzureStorageService azureStorageService;

    public AzureBLOBArchivalJob(AzureStorageService azureStorageService) {
        this.azureStorageService = azureStorageService;
    }
    @Scheduled(cron = "0/10 * * * * *")
    public void archiveTestProducts(){
        System.out.println("ArchivalJob"+azureStorageService.listAllFilesInStorageContainer());
    }
}
