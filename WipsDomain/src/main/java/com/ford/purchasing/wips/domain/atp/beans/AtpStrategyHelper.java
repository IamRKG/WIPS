//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.StrategyOutput;
import com.ford.purchasing.wips.common.atp.StrategyPlantDetail;
import com.ford.purchasing.wips.common.atp.StrategySupplier;
import com.ford.purchasing.wips.common.atp.StrategyTemporaryOutput;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;

@SuppressWarnings("javadoc")
public class AtpStrategyHelper {

    public StrategyOutput getStrategyDetails(final DataArea outputDataArea)
            throws ConnectorException {
        final StrategyOutput output = new StrategyOutput();
        output.setStrategy(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-SRGTYPE"));
        output.setPart(new AtpRecapHelper().getPartNumber(outputDataArea));
        output.setEnggLevel(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-ENGLVL"));
        output.setTrendRate(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-TRENDESC"));
        output.setBuyer(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-JOBCODE"));
        output.setStatus(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-STATUS"));
        output.setAtpType(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-ATPTYPE"));
        return output;
    }

    public List<StrategyTemporaryOutput> getPlants(final DataArea outputDataArea)
            throws ConnectorException {
        final List<StrategyTemporaryOutput> listOfPlants =
                new ArrayList<StrategyTemporaryOutput>();
        final List<DataArea> approversDataList = TransactionOutputUtil
                .getArray(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-BUFFER-SEGLOOP");
        DataArea parameterList;
        for (final Iterator<DataArea> i = approversDataList.iterator(); i.hasNext();) {
            final StrategyTemporaryOutput tempBean = new StrategyTemporaryOutput();
            parameterList = i.next();
            final String plantCode =
                    TransactionOutputUtil.getString(parameterList, "TPO-PLTCDE");
            if (plantCode != null && !"".equals(plantCode)) {
                tempBean.setPlant(getPlantDetail(parameterList, plantCode));
                tempBean.setSupplier(getSupplierDetail(parameterList));
                tempBean.setNationalCompany(getNationalCompany(parameterList));
                final String percent = getPercentage(parameterList);
                tempBean.setPercentage(percent);
                tempBean.setApw(getApw(parameterList, percent));
                listOfPlants.add(tempBean);
            }

        }
        if (!listOfPlants.isEmpty()) {
            Collections.sort(listOfPlants, new Comparator<StrategyTemporaryOutput>() {

                @Override
                public int compare(final StrategyTemporaryOutput object1,
                        final StrategyTemporaryOutput object2) {
                    return object1.getPlant().compareTo(object2.getPlant());
                }
            });
        }

        return listOfPlants;
    }

    private String getApw(final DataArea parameterList, final String percent)
            throws ConnectorException {
        final String volumeString =
                TransactionOutputUtil.getString(parameterList, "TPO-VOLUME");
        String apw = null;
        if (volumeString != null && !"0".equals(volumeString)) {
            final Double volume = Double.parseDouble(volumeString.replace(",", ""));
            final Double percentage = Double.parseDouble(percent.replace(",", ""));
            if (Double.compare(volume, 1.0) == 0) {
                apw = "1";
            } else {
                apw = (int)Math.round((volume * (100 / percentage)) / 47.5) + "";
            }
        }
        if (apw == null) {
            apw = "0";
        }
        return apw;
    }

    private String getPercentage(final DataArea parameterList) throws ConnectorException {
        return TransactionOutputUtil.getString(parameterList, "TPO-PERCNT");
    }

    private String getNationalCompany(final DataArea parameterList)
            throws ConnectorException {
        return TransactionOutputUtil.getString(parameterList, "TPO-NATCDE");
    }

    private String getSupplierDetail(final DataArea parameterList) throws ConnectorException {
        final String code =
                TransactionOutputUtil.getString(parameterList, "TPO-SUPCDE");
        String supplierCode = null;
        if (code != null && !"".equals(code)) {
            supplierCode = code + "-" + TransactionOutputUtil.getString(parameterList,
                    "TPO-SUPNME");
        }
        return supplierCode;
    }

    private String getPlantDetail(final DataArea parameterList, final String plantCode)
            throws ConnectorException {
        return plantCode + "-" + TransactionOutputUtil.getString(parameterList, "TPO-PLTNME");
    }

    public List<StrategyPlantDetail> groupDataByPlant(final List plants) {
        final Map<String, List<StrategySupplier>> map =
                new HashMap<String, List<StrategySupplier>>();
        final List<StrategyPlantDetail> finalObject = new ArrayList<StrategyPlantDetail>();
        for (int count = 0; count < plants.size(); count++) {
            final StrategyTemporaryOutput output = (StrategyTemporaryOutput)plants.get(count);
            final String key = output.getPlant();
            if (map.containsKey(key)) {
                final List<StrategySupplier> list = map.get(key);
                list.add(getStrategySupplier(output));
            } else {
                final List<StrategySupplier> list = new ArrayList<StrategySupplier>();
                list.add(getStrategySupplier(output));
                map.put(key, list);
                final StrategyPlantDetail plantBean = new StrategyPlantDetail();
                String apw = output.getApw();
                if ("0".equals(apw)) {
                    apw = ((StrategyTemporaryOutput)plants.get(count + 1)).getApw();
                }
                plantBean.setApw(apw);
                plantBean.setNationalCompany(output.getNationalCompany());
                plantBean.setPlantName(output.getPlant());
                plantBean.setStrategysuppliers(list);
                finalObject.add(plantBean);
            }
        }
        return finalObject;
    }

    private StrategySupplier getStrategySupplier(final StrategyTemporaryOutput output) {
        final StrategySupplier supplierBean = new StrategySupplier();
        supplierBean.setSupplierCode(output.getSupplier());
        supplierBean.setPercentage(output.getPercentage());
        return supplierBean;
    }

    public String getRankNumber(final DataArea outputDataArea) throws ConnectorException {
        return TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-RANKNUM-CHAR");
    }
}
