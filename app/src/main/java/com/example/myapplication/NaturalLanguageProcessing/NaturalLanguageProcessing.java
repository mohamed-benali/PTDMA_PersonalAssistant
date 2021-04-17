package com.example.myapplication.NaturalLanguageProcessing;

public interface NaturalLanguageProcessing {
    NaturalLanguageProcessing naturalLanguageProcessingInstance = new NaturalLanguageProcessingImplSpanish();

    static NaturalLanguageProcessing getInstance() {
        return naturalLanguageProcessingInstance;
    }

    boolean isCreateTask(String spokenText);
    boolean isDeleteTask(String spokenText);
    boolean isUpdateTask(String spokenText);
    boolean isGoToTask(String spokenText);

    boolean isTaskDone(String spokenText);
    boolean isTaskNotDone(String spokenText);

    String getTaskIdFromText(String spokenText);

    boolean isSetTitle(String spokenText);
    boolean isSetDescription(String spokenText);

    String getTitleFromText(String spokenText);
    String getDescriptionFromText(String spokenText);

    boolean isSave(String spokenText);

    boolean isConfirmation(String spokenText);


    public String normalizeText(String text);

    boolean isDeleteAllTask(String spokenText);

    boolean isShoppingList(String spokenText);



    boolean isCreate(String spokenText);
    boolean isUpdate(String spokenText);
    boolean isDelete(String spokenText);
    boolean isDeleteAll(String spokenText);
    boolean isSeeElement(String spokenText);

    String getIdFromText(String spokenText);
}
