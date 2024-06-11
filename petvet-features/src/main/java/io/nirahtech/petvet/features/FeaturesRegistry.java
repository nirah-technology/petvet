package io.nirahtech.petvet.features;

import java.io.File;

import io.nirahtech.petvet.features.boot.CreateNewHouseFeature;
import io.nirahtech.petvet.features.boot.DetectFirstBootFeature;
import io.nirahtech.petvet.features.boot.implementations.DefaultCreateNewHouseFeatureImpl;
import io.nirahtech.petvet.features.boot.implementations.DefaultDetectFirstBootFeatureImpl;
import io.nirahtech.petvet.features.pets.AdoptAnimalFeature;
import io.nirahtech.petvet.features.pets.ModifyAnimalInformationSheetFeature;
import io.nirahtech.petvet.features.pets.RetrieveAnimalInformationSheetFeature;
import io.nirahtech.petvet.features.pets.RetrieveFarmFeature;
import io.nirahtech.petvet.features.pets.RetrieveHealthBookFeature;
import io.nirahtech.petvet.features.pets.implementations.DefaultAdoptAnimalFeatureImpl;
import io.nirahtech.petvet.features.pets.implementations.DefaultModifyAnimalInformationSheetFeatureImpl;
import io.nirahtech.petvet.features.pets.implementations.DefaultRetrieveAnimalInformationSheetFeatureImpl;
import io.nirahtech.petvet.features.pets.implementations.DefaultRetrieveFarmFeatureImpl;
import io.nirahtech.petvet.features.pets.implementations.DefaultRetrieveHealthBookFeatureImpl;
import io.nirahtech.petvet.features.pharmacy.DetroyCureFeature;
import io.nirahtech.petvet.features.pharmacy.ModifyCureFeature;
import io.nirahtech.petvet.features.pharmacy.PrepareCureFeature;
import io.nirahtech.petvet.features.pharmacy.RetrieveAllPharmaceuticalStocksFeature;
import io.nirahtech.petvet.features.pharmacy.RetrieveCureDetailFeature;
import io.nirahtech.petvet.features.pharmacy.implementations.DefaultDetroyCureFeatureImpl;
import io.nirahtech.petvet.features.pharmacy.implementations.DefaultModifyCureFeatureImpl;
import io.nirahtech.petvet.features.pharmacy.implementations.DefaultPrepareCureFeatureImpl;
import io.nirahtech.petvet.features.pharmacy.implementations.DefaultRetrieveAllPharmaceuticalStocksFeatureImpl;
import io.nirahtech.petvet.features.pharmacy.implementations.DefaultRetrieveCureDetailFeatureImpl;
import io.nirahtech.petvet.features.planner.CancelEventFeature;
import io.nirahtech.petvet.features.planner.ModifyEventFeature;
import io.nirahtech.petvet.features.planner.PlannifyEventFeature;
import io.nirahtech.petvet.features.planner.RetrieveAllEventsFeature;
import io.nirahtech.petvet.features.planner.RetrieveAllPassedEventsFeature;
import io.nirahtech.petvet.features.planner.RetrieveAllUpcommingEventsFeature;
import io.nirahtech.petvet.features.planner.RetrieveEventDetailFeature;
import io.nirahtech.petvet.features.planner.implementations.DefaultCancelEventFeatureImpl;
import io.nirahtech.petvet.features.planner.implementations.DefaultModifyEventFeatureImpl;
import io.nirahtech.petvet.features.planner.implementations.DefaultPlannifyEventFeatureImpl;
import io.nirahtech.petvet.features.planner.implementations.DefaultRetrieveAllEventsFeatureImpl;
import io.nirahtech.petvet.features.planner.implementations.DefaultRetrieveAllPassedEventsFeatureImpl;
import io.nirahtech.petvet.features.planner.implementations.DefaultRetrieveAllUpcommingEventsFeatureImpl;
import io.nirahtech.petvet.features.planner.implementations.DefaultRetrieveEventDetailFeatureImpl;

public final class FeaturesRegistry {

    private static FeaturesRegistry instance = new FeaturesRegistry();

    public static FeaturesRegistry getInstance() {
        return instance;
    }

    private FeaturesRegistry() {
        
    }

    public final DetectFirstBootFeature detectFirstBootFeature(final File workingDirectory) {
        return DefaultDetectFirstBootFeatureImpl.getInstance(workingDirectory);
    }

    public final CreateNewHouseFeature createNewHouseFeature() {
        return DefaultCreateNewHouseFeatureImpl.getInstance();
    }

    public final CancelEventFeature cancelEventFeature() {
        return DefaultCancelEventFeatureImpl.getInstance();
    }
    public final PlannifyEventFeature plannifyEventFeature() {
        return DefaultPlannifyEventFeatureImpl.getInstance();
    }

    public final ModifyEventFeature modifyEventFeature() {
        return DefaultModifyEventFeatureImpl.getInstance();
    }
    
    // NotifyForIncommingEventFeature

    public final RetrieveAllEventsFeature retrieveAllEventsFeature() {
        return DefaultRetrieveAllEventsFeatureImpl.getInstance();
    }
    public final RetrieveAllPassedEventsFeature retrieveAllPassedEventsFeature() {
        return DefaultRetrieveAllPassedEventsFeatureImpl.getInstance();
    }
    public final RetrieveAllUpcommingEventsFeature retrieveAllUpcommingEventsFeature() {
        return DefaultRetrieveAllUpcommingEventsFeatureImpl.getInstance();
    }
    public final RetrieveEventDetailFeature retrieveEventDetailFeature() {
        return DefaultRetrieveEventDetailFeatureImpl.getInstance();
    }

    // ComputeCureDosageForPetFeature

    public final DetroyCureFeature detroyCureFeature() {
        return DefaultDetroyCureFeatureImpl.getInstance();
    }
    public final ModifyCureFeature modifyCureFeature() {
        return DefaultModifyCureFeatureImpl.getInstance();
    }
    public final PrepareCureFeature prepareCureFeature() {
        return DefaultPrepareCureFeatureImpl.getInstance();
    }
    public final RetrieveAllPharmaceuticalStocksFeature retrieveAllPharmaceuticalStocksFeature() {
        return DefaultRetrieveAllPharmaceuticalStocksFeatureImpl.getInstance();
    }
    public final RetrieveCureDetailFeature retrieveCureDetailFeature() {
        return DefaultRetrieveCureDetailFeatureImpl.getInstance();
    }
    
    // AddNewVetConsultationFeature


    public final AdoptAnimalFeature adoptAnimalFeature() {
        return DefaultAdoptAnimalFeatureImpl.getInstance();
    }
    public final ModifyAnimalInformationSheetFeature modifyAnimalInformationSheetFeature() {
        return DefaultModifyAnimalInformationSheetFeatureImpl.getInstance();
    }
    public final RetrieveAnimalInformationSheetFeature retrieveAnimalInformationSheetFeature() {
        return DefaultRetrieveAnimalInformationSheetFeatureImpl.getInstance();
    }
    public final RetrieveFarmFeature retrieveFarmFeature() {
        return DefaultRetrieveFarmFeatureImpl.getInstance();
    }
    public final RetrieveHealthBookFeature retrieveHealthBookFeature() {
        return DefaultRetrieveHealthBookFeatureImpl.getInstance();
    }


}
