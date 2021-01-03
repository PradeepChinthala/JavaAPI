package project.variables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.aspectj.weaver.patterns.EllipsisTypePattern;
import org.junit.Assert;

import net.serenitybdd.core.Serenity;
import project.utilities.ExcelUtils;
import project.utilities.OracleDBUtils;

public class OracleDBQueries {

	public static String getOracleDBQuery(String sInputs){		

		String sba = null;
		switch(sInputs.toUpperCase()){

		case "ELL QUERY":
			sba = "WITH LATEST_REL\r\n" +
					"     AS (SELECT MRCR.VMID_RULE_KEY,\r\n" +
					"                MRCR.MID_RULE_KEY,\r\n" +
					"                TCR.TOPIC_KEY,\r\n" +
					"                TCR.TOPIC_TITLE,\r\n" +
					"                DPCR.DP_KEY,\r\n" +
					"                DPCR.DP_DESC,\r\n" +
					"                DPCR.SORT_ORDER,\r\n" +
					"                MPCR.MED_POL_KEY,\r\n" +
					"                TCR.TOPIC_DESC,\r\n" +
					"                MPCR.MED_POL_TITLE,\r\n" +
					"                TCR.RELEASE_LOG_KEY,\r\n" +
					"                RELEASE_DATE,\r\n" +
					"                RELEASE_NAME,\r\n" +
					"                MPCR.SORT_ORDER MP_SORT_ORDER,\r\n" +
					"                (SELECT DISTINCT PRIM_REF_SOURCE_KEY FROM ELL.VLIBRARY_VERSIONS VLV\r\n" +
					"                WHERE VLV.VMID_RULE_KEY = MRCR.VMID_RULE_KEY) PRIM_REF_SOURCE_KEY,\r\n" +
					"                (SELECT DISTINCT PRIM_REF_SOURCE_DESC FROM ELL.VLIBRARY_VERSIONS VLV\r\n" +
					"                WHERE VLV.VMID_RULE_KEY = MRCR.VMID_RULE_KEY) PRIM_REF_SOURCE_DESC\r\n" +
					"                \r\n" +
					"           FROM ELL.VMID_RULES MRCR,\r\n" +
					"                ELL.VDECISION_POINTS DPCR,\r\n" +
					"                ELL.VTOPICS TCR,\r\n" +
					"                AUTH_MASTER.RELEASE_LOG LCR,\r\n" +
					"                ELL.VMEDICAL_POLICIES MPCR,\r\n" +
					"                AUTH_MASTER.RELEASES R\r\n" +
					"          WHERE     LCR.RELEASE_LOG_KEY = TCR.RELEASE_LOG_KEY\r\n" +
					"                AND MPCR.VMED_POL_KEY = TCR.VMED_POL_KEY\r\n" +
					"                AND TCR.VTOPIC_KEY = DPCR.VTOPIC_KEY\r\n" +
					"                AND DPCR.VDP_KEY = MRCR.VDP_KEY\r\n" +
					"                AND MPCR.RELEASE_LOG_KEY = TCR.RELEASE_LOG_KEY\r\n" +
					"                AND DPCR.RELEASE_LOG_KEY = TCR.RELEASE_LOG_KEY\r\n" +
					"                AND MRCR.RELEASE_LOG_KEY = DPCR.RELEASE_LOG_KEY\r\n" +
					"                AND LCR.RELEASE_KEY = R.RELEASE_KEY\r\n" +
					"                AND TCR.RELEASE_LOG_KEY IN (SELECT RELEASE_LOG_KEY\r\n" +
					"                                              FROM AUTH_MASTER.RELEASE_LOG\r\n" +
					"                                             WHERE     TO_CHAR (RELEASE_DATE,'mm-yyyy') = TO_CHAR (SYSDATE-60 ,'mm-yyyy')\r\n" +
					"                                                   AND RELEASE_TYPE_KEY = 4)),\r\n" +
					"     OLD_REL\r\n" +
					"     AS (SELECT MRCR.VMID_RULE_KEY,\r\n" +
					"                MRCR.MID_RULE_KEY,\r\n" +
					"                TCR.TOPIC_KEY,\r\n" +
					"                TCR.TOPIC_TITLE,\r\n" +
					"                DPCR.DP_KEY,\r\n" +
					"                DPCR.DP_DESC,\r\n" +
					"                DPCR.SORT_ORDER,\r\n" +
					"                MPCR.MED_POL_KEY,\r\n" +
					"                TCR.TOPIC_DESC,\r\n" +
					"                MPCR.MED_POL_TITLE,\r\n" +
					"                TCR.RELEASE_LOG_KEY,\r\n" +
					"                RELEASE_DATE,\r\n" +
					"                RELEASE_NAME,\r\n" +
					"                MPCR.SORT_ORDER MP_SORT_ORDER,\r\n" +
					"                (SELECT DISTINCT PRIM_REF_SOURCE_KEY FROM ELL.VLIBRARY_VERSIONS VLV\r\n" +
					"                WHERE VLV.VMID_RULE_KEY = MRCR.VMID_RULE_KEY) PRIM_REF_SOURCE_KEY,\r\n" +
					"                (SELECT DISTINCT PRIM_REF_SOURCE_DESC FROM ELL.VLIBRARY_VERSIONS VLV\r\n" +
					"                WHERE VLV.VMID_RULE_KEY = MRCR.VMID_RULE_KEY) PRIM_REF_SOURCE_DESC\r\n" +
					"           FROM ELL.VMID_RULES MRCR,\r\n" +
					"                ELL.VDECISION_POINTS DPCR,\r\n" +
					"                ELL.VTOPICS TCR,\r\n" +
					"                AUTH_MASTER.RELEASE_LOG LCR,\r\n" +
					"                ELL.VMEDICAL_POLICIES MPCR,\r\n" +
					"                AUTH_MASTER.RELEASES R\r\n" +
					"          WHERE     LCR.RELEASE_LOG_KEY = TCR.RELEASE_LOG_KEY\r\n" +
					"                AND MPCR.VMED_POL_KEY = TCR.VMED_POL_KEY\r\n" +
					"                AND TCR.VTOPIC_KEY = DPCR.VTOPIC_KEY\r\n" +
					"                AND DPCR.VDP_KEY = MRCR.VDP_KEY\r\n" +
					"                AND MPCR.RELEASE_LOG_KEY = TCR.RELEASE_LOG_KEY\r\n" +
					"                AND DPCR.RELEASE_LOG_KEY = TCR.RELEASE_LOG_KEY\r\n" +
					"                AND MRCR.RELEASE_LOG_KEY = DPCR.RELEASE_LOG_KEY\r\n" +
					"                AND LCR.RELEASE_KEY = R.RELEASE_KEY\r\n" +
					"                AND TCR.RELEASE_LOG_KEY IN (SELECT RELEASE_LOG_KEY\r\n" +
					"                                              FROM AUTH_MASTER.RELEASE_LOG\r\n" +
					"                                             WHERE     TO_CHAR (RELEASE_DATE,'mm-yyyy') = TO_CHAR (SYSDATE-90 ,'mm-yyyy')\r\n" +
					"                                                   AND RELEASE_TYPE_KEY = 4))\r\n" +
					"  SELECT CASE\r\n" +
					"            WHEN MRPR.DP_KEY <> MRCR.DP_KEY\r\n" +
					"            THEN\r\n" +
					"               'Rule Move'\r\n" +
					"            WHEN MRPR.TOPIC_KEY <> MRCR.TOPIC_KEY\r\n" +
					"            THEN\r\n" +
					"               'DP Move'\r\n" +
					"            WHEN MRPR.MED_POL_KEY <> MRCR.MED_POL_KEY\r\n" +
					"            THEN\r\n" +
					"               'Topic Move'\r\n" +
					"            WHEN MRPR.DP_DESC <> MRCR.DP_DESC\r\n" +
					"            THEN\r\n" +
					"               'DP Desc Change'\r\n" +
					"            WHEN MRPR.SORT_ORDER <> MRCR.SORT_ORDER\r\n" +
					"            THEN\r\n" +
					"               'DP Sort Order Change'\r\n" +
					"            WHEN MRPR.MP_SORT_ORDER <> MRCR.MP_SORT_ORDER\r\n" +
					"            THEN\r\n" +
					"               'MP Sort Order Change'               \r\n" +
					"            WHEN MRPR.TOPIC_TITLE <> MRCR.TOPIC_TITLE\r\n" +
					"            THEN\r\n" +
					"               'Topic Title Change'\r\n" +
					"            WHEN DBMS_LOB.COMPARE (MRPR.TOPIC_DESC, MRCR.TOPIC_DESC) != 0\r\n" +
					"            THEN\r\n" +
					"               'Topic Long Desc Change'\r\n" +
					"            ELSE\r\n" +
					"               'New In Release'\r\n" +
					"         END\r\n" +
					"         ELL_CHANGE,\r\n" +
					"            MRCR.VMID_RULE_KEY\r\n" +
					"            --,MRPR.RELEASE_NAME\r\n" +
					"            ,MRCR.RELEASE_NAME \r\n" +
					"            , MRPR.RELEASE_LOG_KEY PREV_REL_LOG_KEY\r\n" +
					"             ,MRCR.RELEASE_LOG_KEY CUR_REL_LOG_KEY\r\n" +
					"      ,MRPR.MED_POL_KEY PREV_MP\r\n" +
					"       ,MRCR.MED_POL_KEY CUR_MP\r\n" +
					"      ,MRPR.MED_POL_TITLE PREV_MP_TITLE\r\n" +
					"      ,MRCR.MED_POL_TITLE CUR_MP_TITLE\r\n" +
					"      ,MRPR.TOPIC_KEY PREV_TOPIC\r\n" +
					"      ,MRCR.TOPIC_KEY CUR_TOPIC\r\n" +
					"      ,MRPR.TOPIC_TITLE PREV_TOPIC_TITLE\r\n" +
					"      ,MRCR.TOPIC_TITLE CUR_TOPIC_TITLE\r\n" +
					"      ,MRPR.TOPIC_DESC PREV_TOPIC_LONG_DESC\r\n" +
					"      ,MRCR.TOPIC_DESC CUR_TOPIC_LONG_DESC\r\n" +
					"      ,MRPR.DP_KEY PREV_DP\r\n" +
					"      ,MRCR.DP_KEY CUR_DP\r\n" +
					"      ,MRPR.DP_DESC PREV_DP_DESC\r\n" +
					"      ,MRCR.DP_DESC CUR_DP_DESC\r\n" +
					"      ,MRPR.MID_RULE_KEY MID_RULE_MOVE_FROM\r\n" +
					"      ,MRCR.MID_RULE_KEY MID_RULE_MOVE_TO\r\n" +
					"      ,MRPR.RELEASE_DATE PREV_RELEASE_DATE   \r\n" +
					"       ,MRCR.RELEASE_DATE CUR_RELEASE_DATE\r\n" +
					"       ,MRPR.SORT_ORDER PREV_DP_SORT_ORDER\r\n" +
					"        ,MRCR.SORT_ORDER CUR_DP_SORT_ORDER\r\n" +
					"        ,MRPR.MP_SORT_ORDER PREV_MP_SORT_ORDER\r\n" +
					"        , MRCR.MP_SORT_ORDER CUR_MP_SORT_ORDER\r\n" +
					"         ,MRCR.PRIM_REF_SOURCE_KEY CUR_REF_SOURCE_KEY\r\n" +
					"      ,MRPR.PRIM_REF_SOURCE_KEY PREV_REF_SOURCE_KEY\r\n" +
					"      ,MRCR.PRIM_REF_SOURCE_DESC CUR_REF_SOURCE_DESC         \r\n" +
					"      ,MRPR.PRIM_REF_SOURCE_DESC PREV_REF_SOURCE_DESC\r\n" +
					"         ,(SELECT LISTAGG (EMR_CODE, ',') WITHIN GROUP (ORDER BY EMR_CODE)\r\n" +
					"            FROM (SELECT EMP.EMR_CODE\r\n" +
					"                    FROM AUTH_MASTER.EMR_MED_POLS EMP\r\n" +
					"                   WHERE     EMP.EMR_CODE IN (SELECT EL.EMR_CODE\r\n" +
					"                                            FROM AUTH_MASTER.EMR_LOG EL\r\n" +
					"                                           WHERE EL.RELEASE_LOG_KEY IN (SELECT RL.RELEASE_LOG_KEY\r\n" +
					"                                              FROM AUTH_MASTER.RELEASE_LOG RL\r\n" +
					"                                             WHERE     TO_CHAR (RL.RELEASE_DATE,'mm-yyyy') = TO_CHAR (SYSDATE-60 ,'mm-yyyy')\r\n" +
					"                                                   AND RL.RELEASE_TYPE_KEY = 4))\r\n" +
					"                         AND EMP.MED_POL_KEY = MRCR.MED_POL_KEY)) EMR_CODES\r\n" +
					"    FROM LATEST_REL MRCR, OLD_REL MRPR\r\n" +
					"   WHERE     MRPR.MID_RULE_KEY(+) = MRCR.MID_RULE_KEY\r\n" +
					"         AND (   MRPR.DP_KEY <> MRCR.DP_KEY\r\n" +
					"              OR MRPR.TOPIC_KEY <> MRCR.TOPIC_KEY\r\n" +
					"              OR MRPR.MED_POL_KEY <> MRCR.MED_POL_KEY\r\n" +
					"              OR MRPR.DP_DESC <> MRCR.DP_DESC\r\n" +
					"              OR MRPR.SORT_ORDER <> MRCR.SORT_ORDER\r\n" +
					"              OR MRPR.MP_SORT_ORDER <> MRCR.MP_SORT_ORDER\r\n" +
					"              OR MRPR.TOPIC_TITLE <> MRCR.TOPIC_TITLE\r\n" +
					"              OR MRPR.PRIM_REF_SOURCE_KEY <> MRCR.PRIM_REF_SOURCE_KEY\r\n" +
					"              OR NOT EXISTS\r\n" +
					"                    (SELECT 1\r\n" +
					"                       FROM OLD_REL L\r\n" +
					"                      WHERE MRCR.MID_RULE_KEY = L.MID_RULE_KEY)\r\n" +
					"              OR DBMS_LOB.COMPARE (MRPR.TOPIC_DESC, MRCR.TOPIC_DESC) != -0)\r\n" +
					"---AND MRCR.VMID_RULE_KEY > ${OFFSET} \r\n" +
					"ORDER BY MRCR.VMID_RULE_KEY\r\n";
			break;
			
		case "DISTINCT PPS OF A CLIENT":
			sba = "select distinct "+Serenity.sessionVariableCalled("PPS")+" from payers.vw_policy_sets where policy_set_key in (select policy_set_key from payer_rules.vw_policy_set_config where payer_key in (select payer_key from payers.payer_lkp where client_key in (select client_key from PAYERS.CLIENTS where client_name='"+Serenity.sessionVariableCalled("ClientName")+"')))";
			break;
		


		case "GET DPS FOR SORT ORDER DIFFERENCE":
			sba = "select dp2.dp_key,dp2.sort_order,dp1.sort_order from ELL.VDECISION_POINTS DP1, ell.vdecision_points dp2 where \r\n" +
					" dp1.release_log_key=1856  and DP2.RELEASE_LOG_KEY = 1857\r\n" +
					"and dp1.dp_key=DP2.DP_KEY\r\n" +
					"and DP1.SORT_ORDER<>DP2.SORT_ORDER";
			
			break;
			
		case "GET TOPICS FOR SORT ORDER DIFFERENCE":
		   sba = " select T2.TOPIC_KEY from ELL.vtopics T1, ell.vtopics T2 where \r\n" +
					" T1.release_log_key=1856  and T2.RELEASE_LOG_KEY = 1857\r\n" +
					"and T1.TOPIC_KEY=T2.TOPIC_KEY\r\n" +
					"and T1.SORT_ORDER<>T2.SORT_ORDER";
		   break;
			
		case "GET MPS FOR SORT ORDER DIFFERENCE":
			sba = " select MP2.MED_POL_KEY FROM ELL.VMEDICAL_POLICIES MP1,ELL.VMEDICAL_POLICIES MP2\r\n" +
					"where MP1.release_log_key=1856 and MP2.release_log_key =1857\r\n" +
					"and MP1.med_pol_key=MP2.med_pol_key\r\n" +
					"and MP1.SORT_ORDER<>MP2.SORT_ORDER";
			
			break;
			
		case "GET DPS FOR RETIRE":
			sba = "select dp_key,topic_key from ELL.VDECISION_POINTS where release_log_key = 1856 and life_cycle_key=2\r\n" +
					"intersect\r\n" +
					"select dp_key,topic_key from ELL.VDECISION_POINTS where release_log_key = 1857 and life_cycle_key=3\r\n" +
					"order by dp_key desc";
			break;
			
		case "GET MPS FOR RETIRE":
			sba = "select VMED_POL_KEY from ELL.VMEDICAL_POLICIES   where release_log_key = 1856 and life_cycle_key=2\r\n" +
					"intersect\r\n" +
					"select VMED_POL_KEY from ELL.VMEDICAL_POLICIES   where release_log_key = 1857 and life_cycle_key=3\r\n" +
					"order by VMED_POL_KEY desc";
			break;
			
		case "GET TPS FOR RETIRE":
			sba = "select topic_key from ELL.VTOPICS  where release_log_key = 1856 and life_cycle_key=2\r\n" +
					"intersect\r\n" +
					"select topic_key from ELL.VTOPICS  where release_log_key = 1857 and life_cycle_key=3\r\n" +
					"order by topic_key desc";
			break;
			
		case "PROD MIDRULE COUNT":
			sba = "select mid_rule_key,count(1)\r\n" +
						" from payer_rules.vw_switches sw, payers.payer_lkp lp\r\n" +
						" where sw.payer_key=lp.payer_key and\r\n" +
						" sw.last_updated >= TO_DATE('11/20/2019', 'MM/DD/YYYY')\r\n" +
						" AND lp.PAYER_key in (select payer_key from PAYERS.PAYER_LKP where payer_status_key in (3,5))\r\n" +
						" and prod_10=-1\r\n" +
						" and sw.sub_rule_key in (select sub_rule_key from ELL.VLIBRARY_VERSIONS where release_log_key=1857)\r\n" +
						" group by mid_rule_key\r\n";

			
			break;
		case "PROD MIDRULE PPS":

			sba = "select sw.mid_rule_key,sw.rule_version,sw.sub_rule_key,sw.payer_key,sw.insurance_key,sw.claim_type,sw.prod_10,sw.test_10,sw.payer_short,SW.INSURANCE_DESC from payer_rules.vw_switches sw, payers.payer_lkp lp\r\n" +
					" where sw.payer_key=lp.payer_key and\r\n" +
					" sw.last_updated >= TO_DATE('11/20/2019', 'MM/DD/YYYY')\r\n" +
					" AND lp.PAYER_key in (select payer_key from PAYERS.PAYER_LKP where payer_status_key in (3,5))\r\n" +
					" and prod_10=-1\r\n" +
					" and sw.sub_rule_key in (select sub_rule_key from ELL.VLIBRARY_VERSIONS where release_log_key=1857)\r\n" +
					" and sw.mid_rule_key=midrule\r\n";

			
			break;
			
		case "DEACTIVATED RULES":

			sba = "select sub_rule_key,to_char(dos_to,'mm-dd-yyyy') as DOS_TO ,deactivated_10,disabled_10, last_updated_ts\r\n" +
					"from rules.sub_rules where sub_rule_key in\r\n" +
					"(select sub_rule_key from ell.vlibrary_versions where release_log_key=1857 )\r\n" +
					"and deactivated_10=-1";

			
			break;
			
			
		case "DISABLED RULES":

			sba = "select sub_rule_key,to_char(dos_to,'mm-dd-yyyy') as DOS_TO,deactivated_10,disabled_10, last_updated_ts \r\n" +
					"from rules.sub_rules where sub_rule_key in\r\n" +
					" (select sub_rule_key from ell.vlibrary_versions where release_log_key=1857 )\r\n" +
					"and disabled_10=-1";

			
			break;
			
		case "RETIRED RULES":

			sba = "select sub_rule_key,to_char(dos_to,'mm-dd-yyyy') as DOS_TO,deactivated_10,disabled_10, last_updated_ts \r\n" +
					"from rules.sub_rules where sub_rule_key in\r\n" +
					" (select sub_rule_key from ell.vlibrary_versions where release_log_key=1857\r\n" +
					" and dos_to=to_date('12/31/9999','mm/dd/yyyy'))\r\n" +
					"and dos_to!=to_date('12/31/9999','mm/dd/yyyy')";

			
			break;
			
		case "NEW RULE VERSION RULES":

			sba = "select vv1.mid_rule_key old_mid_rule,vv1.rule_version old_version,vv2.mid_rule_key new_mid_rule,vv2.rule_version new_version,vv2.sub_rule_key,vv2.release_log_key from ell.vlibrary_versions vv1 join \r\n" +
					"ell.vlibrary_versions vv2 on VV1.MID_RULE_KEY=VV2.MID_RULE_KEY and VV1.RULE_VERSION<>VV2.RULE_VERSION \r\n" +
					"where VV1.RELEASE_LOG_KEY=1856 and VV2.RELEASE_LOG_KEY=1857 ";

			
			break;
		case "APPROVE WITH MOD CPT CODE COUNT":
			sba ="select count(*)  from(select cpt_code,short_desc cpt_desc from cpt_master WHERE cpt_code like '014%' order by 1 ASC)";
			break;
		case "APPROVE WITH MOD ICD CODE COUNT":
			sba ="select count(*) from (select icd_code,icd_desc from diag_master where dv_key = 10)";
			break;
		case "APPROVE WITH MOD REASON CODE COUNT":
			sba ="select count(*) from (select reason_code,reason_desc from reason_lkp)";
			break;
		case "APPROVE WITH MOD AGE CODE COUNT":
			sba ="select count(*) from (select * from AGE_FILTER_LKP where min_vs_max_10 =-1)";	
			break;
		case "APPROVE WITH MOD AGE CODE COUNT0":
			sba ="select count(*) from (select * from AGE_FILTER_LKP where min_vs_max_10 =0)";
			break;	
		case "APPROVE WITH MOD CPT CODE DATA":
			sba ="select distinct cpt_code,short_desc cpt_desc from cpt_master WHERE cpt_code like '014%' order by 1 ASC";
			break;	
		case "APPROVE WITH MOD CPT ICD DATA":
			sba ="select icd_code,icd_desc from diag_master where dv_key = 10 and icd_code like 'S84%' order by 1 ASC";
			break;
		case "APPROVE WITH MOD REASON CODE DATA":
			sba ="select reason_code,reason_desc from reason_lkp where reason_code like 'DXI%' order by 1 ASC";	
			break;
		case "APPROVE WITH MOD ICD CODE DATA":
			sba ="select icd_code,icd_desc from diag_master where dv_key = 10 and icd_code like 'S82%' order by 1 ASC";	
			break;	
		case "APPROVE WITH MOD AGE CODE DATA":
			sba ="select * from AGE_FILTER_LKP where min_vs_max_10 =-1";	
			break;
		case "APPROVE WITH MOD AGE CODE DATA0":
			sba ="select * from AGE_FILTER_LKP where min_vs_max_10 =0";
			break;		
			
		case "DP SORT ORDER CHANGE":
			
			sba = "select distinct DP2.SORT_ORDER PREV_ORDER,DP1.SORT_ORDER CURRENT_ORDER,MR1.MID_RULE_KEY,\r\n" +
					"       mr1.dp_key from\r\n" +
					"           ELL.VMID_RULES MR1\r\n" +
					"       INNER JOIN ELL.VMID_RULES MR2 ON MR2.mid_rule_key=mr1.mid_rule_key\r\n" +
					"       inner join ELL.VDECISION_POINTS dp2 on mr2.dp_key = dp2.dp_key and dp2.release_log_key = 1856\r\n" +
					"       inner join ell.vdecision_points dp1 on mr1.dp_key = DP1.DP_KEY and dp1.release_log_key = 1857\r\n" +
					"       and dp1.sort_order <> dp2.sort_order\r\n" +
					"      where mr1.release_log_key=1857 and mr2.RELEASE_LOG_KEY=1856 ";
			
			break;
			
		case "DP DESC CHANGE":
			
			sba = "select DISTINCT MR1.MID_RULE_KEY,DP2.DP_KEY PREV_DP_KEY,DP1.DP_KEY CUR_DP_KEY ,DP2.DP_DESC PREV_DESC,DP1.DP_DESC CURRENT_DESC from  ELL.VDECISION_POINTS DP1 \r\n" +
					"inner join ELL.VDECISION_POINTS DP2 \r\n" +
					"on DP1.DP_KEY = DP2.DP_KEY and DP1.DP_DESC<>DP2.DP_DESC\r\n" +
					"inner join ell.vmid_rules mr1 on MR1.vDP_KEY=dp1.vDP_KEY\r\n" +
					"INNER JOIN ELL.VMID_RULES MR2 ON MR2.vDP_KEY=DP2.vDP_KEY\r\n" +
					"where DP1.RELEASE_LOG_KEY=1857 and DP2.RELEASE_LOG_KEY=1856";
			
			break;
			
		case "TOPIC LONG DESC CHANGE":
			
			sba = "select  m1.mid_rule_key,t1.topic_key,t1.topic_desc c_topic_desc,T2.TOPIC_DESC o_topic_desc\r\n" +
					"from ELL.VW_ELL_MR_TO_MP m1,ELL.VW_ELL_MR_TO_MP m2, ell.vtopics t1,ell.vtopics t2\r\n" +
					"where m1.release_log_key = 1857\r\n" +
					"and m2.release_log_key = 1856\r\n" +
					"and m1.mid_rule_key = m2.mid_rule_key\r\n" +
					"and m1.topic_key = T1.TOPIC_KEY\r\n" +
					"and m2.topic_key = t2.topic_key\r\n" +
					"and t1.release_log_key = 1857\r\n" +
					"and t2.release_log_key = 1856\r\n" +
					"and DBMS_LOB.COMPARE (t1.TOPIC_DESC,t2.TOPIC_DESC) != 0";
			
			break;
			
		case "TOPIC DESC CHANGE":
			
			sba = "select distinct mr1.mid_rule_key,vt1.topic_title cur_title,VT2.TOPIC_TITLE prev_title from ell.vtopics vt1\r\n" +
					"join ell.vtopics vt2 on vt1.TOPIC_KEY=vt2.topic_key \r\n" +
					"join ell.vdecision_points dp1 on dp1.vtopic_key=vt1.vtopic_key\r\n" +
					"join ell.vdecision_points dp2 on dp2.vtopic_key=vt2.vtopic_key\r\n" +
					"join ELL.VMID_RULES mr1 on MR1.DP_KEY=dp1.dp_key\r\n" +
					"join ell.vmid_rules mr2 on mr2.dp_key=dp2.dp_key\r\n" +
					"where vt1.topic_title<>vt2.topic_title \r\n" +
					"and  vt1.release_log_key=1857 and vt2.release_log_key=1856";
			
			break;
			
		case "RULE RECAT":
			
			sba = "select MR1.DP_KEY CURRENT_DP,MR2.DP_KEY PREV_DP,MR1.MID_RULE_KEY from\r\n" +
					"ELL.VMID_RULES MR1\r\n" +
					"INNER JOIN ELL.VMID_RULES MR2 ON MR2.mid_rule_key=mr1.mid_rule_key\r\n" +
					"where mr1.release_log_key=1857 and mr2.RELEASE_LOG_KEY=1856 AND MR1.DP_KEY<>MR2.DP_KEY";
			
			break;
			
		case "DP RECAT":
			sba = "SELECT DISTINCT MR1.MID_RULE_KEY,DP1.DP_KEY ,VT1.TOPIC_KEY CUR_TOPIC,VT2.TOPIC_KEY PREV_TOPIC  FROM ELL.VDECISION_POINTS DP1\r\n" +
					"  JOIN ELL.VDECISION_POINTS DP2 ON DP1.DP_KEY=DP2.DP_KEY\r\n" +
					"  JOIN ELL.VTOPICS VT1 ON VT1.VTOPIC_KEY=DP1.VTOPIC_KEY\r\n" +
					"  JOIN ELL.VTOPICS VT2 ON VT2.VTOPIC_KEY=DP2.VTOPIC_KEY\r\n" +
					"  JOIN ELL.VMID_RULES MR1 ON MR1.VDP_KEY=DP1.VDP_KEY\r\n" +
					"  JOIN ELL.VMID_RULES MR2 ON MR2.VDP_KEY=DP2.VDP_KEY\r\n" +
					"    where VT1.RELEASE_LOG_KEY=1857 AND VT2.RELEASE_LOG_KEY=1856 AND\r\n" +
					"  DP1.TOPIC_KEY<>DP2.TOPIC_KEY";
			break;
			
		case "TOPIC RECAT":
			sba = "select DISTINCT VT2.TOPIC_KEY,VT2.MED_POL_KEY CUR_MP_KEY,VT1.MED_POL_KEY PRE_MP_KEY\r\n" +
					"FROM ELL.VMEDICAL_POLICIES MP1\r\n" +
					"JOIN ELL.VTOPICS VT1 ON VT1.VMED_POL_KEY=MP1.VMED_POL_KEY\r\n" +
					"JOIN ELL.VTOPICS VT2  ON  VT1.topic_KEY=VT2.topic_KEY\r\n" +
					" JOIN ELL.VMEDICAL_POLICIES MP2 ON vt2.VMED_POL_KEY=MP2.VMED_POL_KEY\r\n" +
					"WHERE  VT1.MED_POL_KEY<>VT2.MED_POL_KEY\r\n" +
					"AND  MP1.release_log_key=1856 and MP2.release_log_key =1857";
			break;

		case "TOPIC SORT ORDER":
			sba = "select DISTINCT T2.TOPIC_KEY,T1.SORT_ORDER OLD_SORT,T2.SORT_ORDER NEW_SORT from ELL.vtopics T1, ell.vtopics T2 where \r\n" +
					" T1.release_log_key=1858  and T2.RELEASE_LOG_KEY = 1859\r\n" +
					"and T1.TOPIC_KEY=T2.TOPIC_KEY\r\n" +
					"and T1.SORT_ORDER<>T2.SORT_ORDER";
			break;

		case "MP SORT ORDER":
			sba = "select MP2.MED_POL_KEY FROM ELL.VMEDICAL_POLICIES MP1,ELL.VMEDICAL_POLICIES MP2\r\n" +
					"where MP1.release_log_key=1856 and MP2.release_log_key =1857\r\n" +
					"and MP1.med_pol_key=MP2.med_pol_key\r\n" +
					"and MP1.SORT_ORDER<>MP2.SORT_ORDER";
			break;

		case "DP RETIRE":
			sba = "select dp_key,topic_key from ELL.VDECISION_POINTS where release_log_key = 1858 and life_cycle_key=2\r\n" +
					"intersect\r\n" +
					"select dp_key,topic_key from ELL.VDECISION_POINTS where release_log_key = 1859 and life_cycle_key=3\r\n" +
					"order by dp_key desc";
			break;

		case "TOPIC RETIRE":
			sba = "select topic_key from ELL.VTOPICS  where release_log_key = 1858 and life_cycle_key=2\r\n" +
					"intersect\r\n" +
					"select topic_key from ELL.VTOPICS  where release_log_key = 1859 and life_cycle_key=3\r\n" +
					"order by topic_key desc";
			break;

		case "MP RETIRE":
			sba = "select MED_POL_KEY from ELL.VMEDICAL_POLICIES   where release_log_key = 1858 and life_cycle_key=2\r\n" +
					"intersect\r\n" +
					"select MED_POL_KEY from ELL.VMEDICAL_POLICIES   where release_log_key = 1859 and life_cycle_key=3\r\n" +
					"order by MED_POL_KEY desc";

			break;

		case "RULE DEACTIVATE":
			sba = "select sub_rule_key,mid_rule_key,dos_to,deactivated_10,disabled_10,last_updated_ts from rules.sub_rules where sub_rule_key in\r\n" +
					"(select sub_rule_key from ell.vlibrary_versions where release_log_key=1857)and deactivated_10=-1";
			break;
			
		case "RULE DISABLED":
			sba = "select sub_rule_key,dos_to,deactivated_10,disabled_10,last_updated_ts from rules.sub_rules where sub_rule_key in\r\n" +
					"(select sub_rule_key from ell.vlibrary_versions where release_log_key=1857 )and disabled_10=-1;\r\n";
			break;

		case  "NEW MIDRULE":
			sba = "With A as (select MP.MED_POL_KEY,MP.RELEASE_LOG_KEY,MP.SORT_ORDER MO,MP.MED_POL_TITLE,VP.TOPIC_KEY,VP.TOPIC_TITLE,VP.TOPIC_DESC,DP.DP_KEY,DP.SORT_ORDER DSO,DP.DP_DESC,MR.MID_RULE_KEY,vl.rule_version\r\n" +
				"   from ELL.VMEDICAL_POLICIES MP\r\n" +
				"   JOIN ELL.VTOPICS VP ON MP.VMED_POL_KEY=VP.VMED_POL_KEY\r\n" +
				"   JOIN ELL.VDECISION_POINTS DP ON VP.VTOPIC_KEY=DP.VTOPIC_KEY\r\n" +
				"   JOIN ELL.VMID_RULES MR ON MR.VDP_KEY=DP.VDP_KEY\r\n" +
				"   join ELL.VLIBRARY_VERSIONS vl on VL.VMID_RULE_KEY = mr.vmid_rule_key\r\n" +
				"    where MP.release_log_key = 1859),\r\n" +
				"    B as\r\n" +
				"      (select MP.MED_POL_KEY,MP.RELEASE_LOG_KEY,MP.SORT_ORDER MO,MP.MED_POL_TITLE,DP.TOPIC_KEY,VP.TOPIC_TITLE,VP.TOPIC_DESC,MR.DP_KEY,DP.SORT_ORDER DSO,DP.DP_DESC,MR.MID_RULE_KEY,vl.rule_version from ELL.VMEDICAL_POLICIES MP\r\n" +
				"   JOIN ELL.VTOPICS VP ON MP.VMED_POL_KEY=VP.VMED_POL_KEY\r\n" +
				"   JOIN ELL.VDECISION_POINTS DP ON VP.VTOPIC_KEY=DP.VTOPIC_KEY\r\n" +
				"   JOIN ELL.VMID_RULES MR ON MR.VDP_KEY=DP.VDP_KEY\r\n" +
				"      join ELL.VLIBRARY_VERSIONS vl on VL.VMID_RULE_KEY = mr.vmid_rule_key\r\n" +
				"    where MP.release_log_key = 1858)\r\n" +
				" select\r\n" +
				"distinct A.MED_POL_TITLE,A.TOPIC_KEY,A.TOPIC_TITLE,A.DP_KEY,A.MID_RULE_KEY,A.rule_version\r\n" +
				" from a,b\r\n" +
				"where not exists (select 1 from B\r\n" +
				"    where a.mid_rule_key =b.mid_rule_key)";
			break;
			
		case  "NEW MIDRULE VERSION":
			sba = "select vv1.mid_rule_key old_mid_rule,vv1.rule_version old_version,vv2.mid_rule_key new_mid_rule,vv2.rule_version new_version,vv2.sub_rule_key,vv2.release_log_key from ell.vlibrary_versions vv1 join \r\n" +
					"ell.vlibrary_versions vv2 on VV1.MID_RULE_KEY=VV2.MID_RULE_KEY and VV1.RULE_VERSION<>VV2.RULE_VERSION \r\n" +
					"where VV1.RELEASE_LOG_KEY=1856 and VV2.RELEASE_LOG_KEY=1857";
			break;
			
		case "SWITCH":
			 sba = "select distinct *  from PAYER_RULES.vw_SWITCHES vs\r\n" +
					"join payers.payer_lkp pl on PL.PAYER_KEY=vs.payer_key\r\n" +
					"where   pl.payer_status_key in (3,5) and  pl.client_key=3\r\n" +
					"and vs.last_updated >sysdate-30\r\n" +
					" and ( vs.prod_10=-1 or vs.test_10=-1)";
			 break;
			
		case "PRIMARY SOURCE KEY":
			sba = "SELECT DISTINCT LV1.MID_RULE_KEY,LV1.PRIM_REF_SOURCE_KEY CUR_PRIM_KEY,LV2.PRIM_REF_SOURCE_KEY PREV_PRIM_KEY\r\n" +
					" FROM ELL.VLIBRARY_VERSIONS LV1 \r\n" +
					" INNER JOIN ELL.VLIBRARY_VERSIONS LV2 ON LV1.VMID_RULE_KEY=LV2.VMID_RULE_KEY\r\n" +
					" WHERE LV1.RELEASE_LOG_KEY=1857 AND LV2.RELEASE_LOG_KEY=1856 AND LV1.PRIM_REF_SOURCE_KEY<>LV2.PRIM_REF_SOURCE_KEY";
			break;
			
		case "PRIMARY SOURCE DESCRIPTION":
			sba = "SELECT DISTINCT LV1.MID_RULE_KEY,LV1.PRIM_REF_SOURCE_DESC CUR_PRIM,LV2.PRIM_REF_SOURCE_DESC PREV_PRIM\r\n" +
					" FROM ELL.VLIBRARY_VERSIONS LV1 \r\n" +
					" INNER JOIN ELL.VLIBRARY_VERSIONS LV2 ON LV1.VMID_RULE_KEY=LV2.VMID_RULE_KEY\r\n" +
					" WHERE LV1.RELEASE_LOG_KEY=1857 AND LV2.RELEASE_LOG_KEY=1856 AND LV1.PRIM_REF_SOURCE_DESC<>LV2.PRIM_REF_SOURCE_DESC";
			break;
			
		case "SAVINGS":
			sba = "select R1.client_name,R1.med_policy_key,R1.dp_key,r1.claIm_type old_claim_type,r1.insurance_desc old_ins,r1.annl_raw_savings old_raw,r1.annl_agg_savings old_agg,\r\n" +
					"r1.annl_con_savings old_con,r1.total_edits old_edits,r2.claim_type new_claim_type,r2.insurance_desc new_ins,r2.annl_raw_savings new_raw,r2.annl_agg_savings new_agg,r2.annl_con_savings new_con,r2.total_edits new_edits\r\n" +
					"from RVA_IA.VW_RVA_CPD R1 ,RVA_IA.VW_RVA_CPD R2 where R1.data_version='PMPRD1_20191023_185426' AND R2.DATA_VERSION='PMPRD1_20191120_191234'\r\n" +
					"and R1.DP_KEY=R2.DP_KEY and R1.PAYER_KEY=R2.PAYER_KEY AND R1.CLAIM_TYPE=R2.CLAIM_TYPE AND R1.INSURANCE_KEY=R2.INSURANCE_KEY \r\n" +
					"and R1.annl_RAW_SAVINGS <>  R2.annl_RAW_SAVINGS and r1.rule_in_baseline_10=0 and r2.rule_in_baseline_10=0 and r1.client_key=53 and r2.client_key=53";
			
			break;
			
		case  "NEW RULE VERSION UPDATE AND NOTIFY":
			sba = "select *  from PAYER_RULES.vw_SWITCHES vs\r\n" +
			"join payers.vw_policy_sets ps on ps.PAYER_KEY=vs.payer_key\r\n" +
			"join payers.payer_lkp pl on PL.PAYER_KEY=ps.payer_key\r\n" +
			"where   pl.payer_status_key in (3,5) and  pl.client_key="+Serenity.sessionVariableCalled("ClientKey")+" and vs.mid_rule_key="+Serenity.sessionVariableCalled("MidRuleKey").toString()+" and vs.rule_version="+Serenity.sessionVariableCalled("NewVersion").toString()+"";
			break;
			
		case "DP WITH CONFIGURATION AND INFORMATIONAL":
			sba="select count(vd.dp_key) from Ell.VDECISION_POINTS VD, ell.VTOPICS VT, ELL.VMEDICAL_POLICIES VM where vd.vtopic_key=vt.vtopic_key " +
					"and VT.Vmed_pol_key=VM.Vmed_pol_key " +
					"and VM.release_log_key= " +
					"( " +
					"select release_log_key from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name  " +
					"from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3 " +
					"    where r1.comments like '%eLL%' and r1.release_type_key =4 " +
					"and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key " +
					"order by r1.release_date  desc) where rownum=1 " +
					") " +
					"and vd.life_cycle_key in (1,2)  " +
					"and vd.dp_type_key in (2,3) ";
			break;
		case "DP WITH ALL":
			sba="select count(VMD.vdp_key) from Ell.VMID_RULES VMD, ELL.VDECISION_POINTS VD, ell.VTOPICS VT, ELL.VMEDICAL_POLICIES VM where VMD.vdp_key=VD.vdp_key " +
					"and vd.vtopic_key=vt.vtopic_key " +
					"and VT.Vmed_pol_key=VM.Vmed_pol_key " +
					"and VM.release_log_key= " +
					"( " +
					"select release_log_key from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name  " +
					"from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3 " +
					"    where r1.comments like '%eLL%' and r1.release_type_key =4 " +
					"and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key " +
					"order by r1.release_date  desc) where rownum=1 " +
					") " +
					"and vd.life_cycle_key in (1,2) " +
					"and vmd.dp_key in  " +
					"( " +
					"select VD.dp_key from Ell.VDECISION_POINTS VD, ell.VTOPICS VT, ELL.VMEDICAL_POLICIES VM where vd.vtopic_key=vt.vtopic_key " +
					"and VT.Vmed_pol_key=VM.Vmed_pol_key " +
					"and VM.release_log_key= " +
					"( " +
					"select release_log_key from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name  " +
					"from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3 " +
					"    where r1.comments like '%eLL%' and r1.release_type_key =4 " +
					"and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key " +
					"order by r1.release_date  desc) where rownum=1 " +
					") " +
					"and vd.life_cycle_key in (1,2)  " +
					"and vd.dp_type_key in (2,3) " +
					") ";
			break;
		case "SORT ORDER WITH 1":
			sba="select count(vd.sort_order) from Ell.VDECISION_POINTS VD, ell.VTOPICS VT, ELL.VMEDICAL_POLICIES VM where vd.vtopic_key=vt.vtopic_key " +
					"and VT.Vmed_pol_key=VM.Vmed_pol_key " +
					"and VM.release_log_key= " +
					"( " +
					"select release_log_key from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name  " +
					"from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3 " +
					"    where r1.comments like '%eLL%' and r1.release_type_key =4 " +
					"and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key " +
					"order by r1.release_date  desc) where rownum=1 " +
					") " +
					"and vd.life_cycle_key in (1,2)  " +
					"and vd.dp_type_key in (2,3)  " +
					"and vd.sort_order=1 ";
			break;
		case "SORT ORDER WITHOUT 1":
			sba="select count(vd.sort_order) from Ell.VDECISION_POINTS VD, ell.VTOPICS VT, ELL.VMEDICAL_POLICIES VM where vd.vtopic_key=vt.vtopic_key " +
					"and VT.Vmed_pol_key=VM.Vmed_pol_key " +
					"and VM.release_log_key= " +
					"( " +
					"select release_log_key from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name  " +
					"from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3 " +
					"    where r1.comments like '%eLL%' and r1.release_type_key =4 " +
					"and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key " +
					"order by r1.release_date  desc) where rownum=1 " +
					") " +
					"and vd.life_cycle_key in (1,2)  " +
					"and vd.dp_type_key in (2,3)  " +
					"and vd.sort_order!=1 ";
			break;
			
		case "TEST RULES":
			
				 sba = "with rk as (select * from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name  from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3\r\n" +
				"    where r1.comments like '%eLL%' and r1.release_type_key =4\r\n" +
				" and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key\r\n" +
				" order by r1.release_date  desc) where release_log_key=2019 and rownum=1),\r\n" +
				"    lr as (\r\n" +
				"    SELECT       m.med_pol_title,\r\n" +
				"                 m.med_pol_key,\r\n" +
				"                 t.topic_key,\r\n" +
				"                 t.topic_title,\r\n" +
				"                 t.topic_desc,\r\n" +
				"                 d.dp_key,\r\n" +
				"                 d.dp_desc,\r\n" +
				"                 d.sort_order,\r\n" +
				"                 d.release_log_key,\r\n" +
				"                 r.mid_rule_key,\r\n" +
				"                 l.rule_version,\r\n" +
				"                 l.sub_rule_key,\r\n" +
				"                NVL( ( select -1  from rules.sub_rules sr\r\n" +
				"                where sr.sub_rule_key = l.sub_rule_key\r\n" +
				"                and exists(select 1 from PAYER_RULES.VW_SWITCHES sw\r\n" +
				"                  where sr.sub_rule_key =sw.sub_rule_key and  sw.PAYER_KEY = 1298 )   ) ,0) RVA_10\r\n" +
				"                 FROM ELL.VMEDICAL_POLICIES m,\r\n" +
				"                ELL.VTOPICS t,\r\n" +
				"                ELL.VDECISION_POINTS d,\r\n" +
				"                ELL.VMID_RULES r,\r\n" +
				"                ELL.VLIBRARY_VERSIONS l\r\n" +
				"              WHERE m.VMED_POL_KEY = t.VMED_POL_KEY\r\n" +
				"                AND t.VTOPIC_KEY = d.VTOPIC_KEY\r\n" +
				"                AND d.VDP_KEY = r.VDP_KEY\r\n" +
				"               AND r.VMID_RULE_KEY = l.VMID_RULE_KEY\r\n" +
				"               and d.dp_type_key in (0,1)\r\n" +
				"               And r.cv_key=1),\r\n" +
				"            \r\n" +
				"  rv as (select lr.med_pol_key,lr.med_pol_title,lr.topic_key,lr.topic_title,lr.topic_desc,lr.dp_key, lr.dp_desc,lr.mid_rule_key ,\r\n" +
				" lr.rule_version,lr.sub_rule_key ,lr.release_log_key,lr.sort_order,rk.release_date,rk.release_name, RVA_10 from  rk ,lr\r\n" +
				" where rk.release_log_key =lr.release_log_key)\r\n" +
				" select * from rv";

			 
			 break;
			 
		case "LATEST COLLECTION":
			
			 // BuildMyString.com generated code. Please enjoy your string responsibly.

			sba = "with rk as (select * from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name  from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3\r\n" +
			"    where r1.comments like '%eLL%' and r1.release_type_key =4\r\n" +
			"and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key\r\n" +
			"order by r1.release_date  desc) where release_log_key=1410 and rownum=1),\r\n" +
			"    lr as (\r\n" +
			"    SELECT       m.med_pol_title,\r\n" +
			"                 m.med_pol_key,\r\n" +
			"                 t.topic_key,\r\n" +
			"                 t.topic_title,\r\n" +
			"                 t.topic_desc,\r\n" +
			"                 d.dp_key,\r\n" +
			"                 d.dp_desc,\r\n" +
			"                 d.dp_type_key,\r\n" +
			"                 d.sort_order,\r\n" +
			"                 d.release_log_key,\r\n" +
			"                 r.mid_rule_key,\r\n" +
			"                 l.rule_version,\r\n" +
			"                 l.sub_rule_key,\r\n" +
			"                 l.sub_rule_desc,\r\n" +
			"                 l.sub_rule_notes,\r\n" +
			"                 s.library_status_key,\r\n" +
			"                 s.rule_header_key,\r\n" +
			"                 s.reference,\r\n" +
			"                 s.gender_cat_key,\r\n" +
			"                 l.reason_code,\r\n" +
			"                 l.bw_reason_code,\r\n" +
			"                 l.prim_core_enhanced_key,\r\n" +
			"                 l.prim_core_enhanced_desc,\r\n" +
			"                 l.prim_ref_source_key,\r\n" +
			"                 l.prim_ref_source_desc,\r\n" +
			"                 l.prim_ref_title_key,\r\n" +
			"                 l.prim_ref_title_desc,\r\n" +
			"                NVL( ( select -1  from rules.sub_rules sr\r\n" +
			"                where sr.sub_rule_key = l.sub_rule_key\r\n" +
			"                and exists(select 1 from PAYER_RULES.VW_SWITCHES sw\r\n" +
			"                  where sr.sub_rule_key =sw.sub_rule_key and  sw.PAYER_KEY = 1298 )   ) ,0) RVA_10\r\n" +
			"                 FROM ELL.VMEDICAL_POLICIES m,\r\n" +
			"                ELL.VTOPICS t,\r\n" +
			"                ELL.VDECISION_POINTS d,\r\n" +
			"                ELL.VMID_RULES r,\r\n" +
			"                ELL.VLIBRARY_VERSIONS l,\r\n" +
			"                RULES.sub_rules s\r\n" +
			"              WHERE m.VMED_POL_KEY = t.VMED_POL_KEY\r\n" +
			"                AND t.VTOPIC_KEY = d.VTOPIC_KEY\r\n" +
			"                AND d.VDP_KEY = r.VDP_KEY\r\n" +
			"               AND r.VMID_RULE_KEY = l.VMID_RULE_KEY\r\n" +
			"               and l.SUB_RULE_KEY = s.SUB_RULE_KEY\r\n" +
			"               and d.dp_type_key in (0,1)\r\n" +
			"               And r.cv_key=1),\r\n" +
			"            \r\n" +
			"  rv as (select lr.med_pol_key,lr.med_pol_title,lr.topic_key,lr.topic_title,lr.topic_desc,lr.dp_key, lr.dp_desc,  lr.dp_type_key, lr.sort_order as DP_SORT_ORDER,lr.mid_rule_key ,\r\n" +
			" lr.rule_version,lr.sub_rule_key ,lr.sub_rule_desc,lr.sub_rule_notes,lr.library_status_key,lr.reason_code,lr.bw_reason_code,lr.rule_header_key,lr.prim_core_enhanced_key,\r\n" +
			" lr.prim_core_enhanced_desc,lr.prim_ref_source_key,lr.prim_ref_source_desc,lr.prim_ref_title_key,lr.prim_ref_title_desc,lr.reference,lr.gender_cat_key,\r\n" +
			" lr.release_log_key,rk.release_name,rk.release_date, RVA_10 from  rk ,lr\r\n" +
			"  where rk.release_log_key =lr.release_log_key) \r\n" +
			"  select * from rv  where dp_key=2191 and dp_type_key=1";
			
			break;
			
		case "INFORMATIONAL DP":
			
			sba = "with rk as (select * from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name  from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3\r\n" +
					"    where r1.comments like '%eLL%' and r1.release_type_key =4\r\n" +
					"and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key\r\n" +
					"order by r1.release_date  desc) where release_log_key=697 and rownum=1),\r\n" +
					"    lr as (\r\n" +
					"    SELECT       m.med_pol_title,\r\n" +
					"                 m.med_pol_key,\r\n" +
					"                 t.topic_key,\r\n" +
					"                 t.topic_title,\r\n" +
					"                 t.topic_desc,\r\n" +
					"                 d.dp_key,\r\n" +
					"                 d.dp_desc,\r\n" +
					"                 d.dp_type_key,\r\n" +
					"                 d.sort_order,\r\n" +
					"                 d.release_log_key,\r\n" +
					"                 mr.mid_rule_key,\r\n" +
					"                 l.rule_version,\r\n" +
					"                 l.sub_rule_key,\r\n" +
					"                 l.sub_rule_desc,\r\n" +
					"                 l.sub_rule_notes,\r\n" +
					"                 s.library_status_key,\r\n" +
					"                 s.rule_header_key,\r\n" +
					"                 s.reference,\r\n" +
					"                 s.gender_cat_key,\r\n" +
					"                 l.reason_code,\r\n" +
					"                 l.bw_reason_code,\r\n" +
					"                 l.prim_core_enhanced_key,\r\n" +
					"                 l.prim_core_enhanced_desc,\r\n" +
					"                 l.prim_ref_source_key,\r\n" +
					"                 l.prim_ref_source_desc,\r\n" +
					"                 l.prim_ref_title_key,\r\n" +
					"                 l.prim_ref_title_desc\r\n" +
					"                 FROM ELL.VMEDICAL_POLICIES m,\r\n" +
					"                ELL.VTOPICS t,\r\n" +
					"                ELL.VDECISION_POINTS d,\r\n" +
					"                ELL.VMID_RULES mr,\r\n" +
					"                ELL.VLIBRARY_VERSIONS l,\r\n" +
					"                RULES.sub_rules s,\r\n" +
					"                AUTH_MASTER.RELEASE_LOG rl,\r\n" +
					"                AUTH_MASTER.RELEASES r\r\n" +
					"              WHERE \r\n" +
					"              rl.RELEASE_log_KEY = t.RELEASE_log_KEY\r\n" +
					"              And m.VMED_POL_KEY = t.VMED_POL_KEY\r\n" +
					"                AND t.VTOPIC_KEY = d.VTOPIC_KEY\r\n" +
					"                AND d.VDP_KEY = mr.VDP_KEY (+)\r\n" +
					"               AND l.VMID_RULE_KEY (+) = mr.VMID_RULE_KEY\r\n" +
					"               and l.SUB_RULE_KEY = s.SUB_RULE_KEY (+)\r\n" +
					"               AND rl.RELEASE_KEY = r.RELEASE_KEY\r\n" +
					"               and d.dp_type_key in(3)\r\n" +
					"                            \r\n" +
					"               ),\r\n" +
					"            \r\n" +
					"  rv as (select lr.med_pol_key,lr.med_pol_title,lr.topic_key,lr.topic_title,lr.topic_desc,lr.dp_key,lr.dp_desc,lr.dp_type_key,lr.sort_order  as dp_sort_order,lr.mid_rule_key ,\r\n" +
					"lr.rule_version,lr.sub_rule_key ,lr.sub_rule_desc,lr.sub_rule_notes,lr.library_status_key,lr.reason_code,lr.bw_reason_code,lr.rule_header_key,lr.prim_core_enhanced_key,\r\n" +
					"lr.prim_core_enhanced_desc,lr.prim_ref_source_key,lr.prim_ref_source_desc,lr.prim_ref_title_key,lr.prim_ref_title_desc,lr.reference,lr.gender_cat_key,\r\n" +
					"lr.release_log_key,rk.release_name,rk.release_date from  rk ,lr\r\n" +
					"  where rk.release_log_key =lr.release_log_key) \r\n" +
					"  select * from rv  where dp_key=5478";


		break;
		
		case "CONFIGURATION DP":
			
			sba = "with rk as (select * from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name  from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3\r\n" +
					"    where r1.comments like '%eLL%' and r1.release_type_key =4\r\n" +
					"and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key\r\n" +
					"order by r1.release_date  desc) where release_log_key=697 and rownum=1),\r\n" +
					"    lr as (\r\n" +
					"    SELECT       m.med_pol_title,\r\n" +
					"                 m.med_pol_key,\r\n" +
					"                 t.topic_key,\r\n" +
					"                 t.topic_title,\r\n" +
					"                 t.topic_desc,\r\n" +
					"                 d.dp_key,\r\n" +
					"                 d.dp_desc,\r\n" +
					"                 d.dp_type_key,\r\n" +
					"                 d.sort_order,\r\n" +
					"                 d.release_log_key,\r\n" +
					"                 mr.mid_rule_key,\r\n" +
					"                 l.rule_version,\r\n" +
					"                 l.sub_rule_key,\r\n" +
					"                 l.sub_rule_desc,\r\n" +
					"                 l.sub_rule_notes,\r\n" +
					"                 s.library_status_key,\r\n" +
					"                 s.rule_header_key,\r\n" +
					"                 s.reference,\r\n" +
					"                 s.gender_cat_key,\r\n" +
					"                 l.reason_code,\r\n" +
					"                 l.bw_reason_code,\r\n" +
					"                 l.prim_core_enhanced_key,\r\n" +
					"                 l.prim_core_enhanced_desc,\r\n" +
					"                 l.prim_ref_source_key,\r\n" +
					"                 l.prim_ref_source_desc,\r\n" +
					"                 l.prim_ref_title_key,\r\n" +
					"                 l.prim_ref_title_desc\r\n" +
					"                 FROM ELL.VMEDICAL_POLICIES m,\r\n" +
					"                ELL.VTOPICS t,\r\n" +
					"                ELL.VDECISION_POINTS d,\r\n" +
					"                ELL.VMID_RULES mr,\r\n" +
					"                ELL.VLIBRARY_VERSIONS l,\r\n" +
					"                RULES.sub_rules s,\r\n" +
					"                AUTH_MASTER.RELEASE_LOG rl,\r\n" +
					"                AUTH_MASTER.RELEASES r\r\n" +
					"              WHERE \r\n" +
					"              rl.RELEASE_log_KEY = t.RELEASE_log_KEY\r\n" +
					"              And m.VMED_POL_KEY = t.VMED_POL_KEY\r\n" +
					"                AND t.VTOPIC_KEY = d.VTOPIC_KEY\r\n" +
					"                AND d.VDP_KEY = mr.VDP_KEY (+)\r\n" +
					"               AND l.VMID_RULE_KEY (+) = mr.VMID_RULE_KEY\r\n" +
					"               and l.SUB_RULE_KEY = s.SUB_RULE_KEY (+)\r\n" +
					"               AND rl.RELEASE_KEY = r.RELEASE_KEY\r\n" +
					"               and d.dp_type_key in(2)\r\n" +
					"                            \r\n" +
					"               ),\r\n" +
					"            \r\n" +
					"  rv as (select lr.med_pol_key,lr.med_pol_title,lr.topic_key,lr.topic_title,lr.topic_desc,lr.dp_key,lr.dp_desc,lr.dp_type_key,lr.sort_order  as dp_sort_order,lr.mid_rule_key ,\r\n" +
					"lr.rule_version,lr.sub_rule_key ,lr.sub_rule_desc,lr.sub_rule_notes,lr.library_status_key,lr.reason_code,lr.bw_reason_code,lr.rule_header_key,lr.prim_core_enhanced_key,\r\n" +
					"lr.prim_core_enhanced_desc,lr.prim_ref_source_key,lr.prim_ref_source_desc,lr.prim_ref_title_key,lr.prim_ref_title_desc,lr.reference,lr.gender_cat_key,\r\n" +
					"lr.release_log_key,rk.release_name,rk.release_date from  rk ,lr\r\n" +
					"  where rk.release_log_key =lr.release_log_key) \r\n" +
					"  select * from rv  where dp_key=4725";

		break;
		case "RELEASE LOG KEY":
			sba="select * from (select r1.release_log_key , to_char(to_date(r1.release_date,'DD-MON-YYY'),'YYYYMMDD')release_date ,r2.release_name " +
					"from AUTH_MASTER.release_log  r1 ,AUTH_MASTER.Releases r2 ,Ell.vmid_rules r3 " +
					"    where r1.comments like '%eLL%' and r1.release_type_key =4 " +
					"and r1.release_key =r2.release_key and r1.release_log_key =r3.release_log_key " +
					"order by r1.release_date  desc) where rownum=1 ";
			break;
			
		default:
			Assert.assertTrue("Given selection was not found ==>"+sInputs, false);
			break;
		}

		System.out.println(sba);		

		return sba;
	}

	//===============================================================================================================================================>

	//################################################### eLL update Queries ##################################################3
	
	
	

	public static String getOracleDBQuery(String sInputs,String sVal1, String sVal2,String sVal3){      

	       String sba = null;
	       switch(sInputs.toUpperCase()){
	       
	       case "PREVIOUS DP DESC CHANGE":
	           sba = "select DP_DESC from ELL.VDECISION_POINTS where DP_KEY =  "+sVal1+" and RELEASE_LOG_KEY = "+sVal2;           
	           break;
	           
	       case "UPDATE DP DESC CHANGE":
	           sba = "UPDATE ELL.VDECISION_POINTS SET DP_DESC='TEST' WHERE DP_KEY= "+sVal1+" AND RELEASE_LOG_KEY="+sVal2;
	           break;
	           
	       case "PREVIOUS TOPIC TITLE CHANGE":
	           sba = " select TOPIC_TITLE from ELL.VTOPICS where TOPIC_KEY =  "+sVal1+" and RELEASE_LOG_KEY = "+sVal2;           
	           break;
	           
	       case "UPDATE TOPIC TITLE CHANGE":
	           sba = "UPDATE ELL.VTOPICS SET TOPIC_TITLE = 'TEST' WHERE TOPIC_KEY = "+sVal1+" AND RELEASE_LOG_KEY="+sVal2;
	           break;
	           
	       case "PREVIOUS TOPIC DESC CHANGE":
	           sba = " select TOPIC_DESC from ELL.VTOPICS where TOPIC_KEY =  "+sVal1+" and RELEASE_LOG_KEY = "+sVal2;           
	           break;
	           
	       case "UPDATE TOPIC DESC CHANGE":
	           sba = "UPDATE ELL.VTOPICS SET TOPIC_DESC = 'TEST AUTOMATION' WHERE TOPIC_KEY = "+sVal1+" AND RELEASE_LOG_KEY="+sVal2;
	           break;
	           
	       case "PREVIOUS MP TITLE CHANGE":
	           sba = "select MED_POL_TITLE from ELL.VMEDICAL_POLICIES where MED_POL_KEY = "+sVal1+" and  RELEASE_LOG_KEY = "+sVal2;         
	           break;
	           
	       case "UPDATE MP TITLE CHANGE":
	           sba = "UPDATE ELL.VMEDICAL_POLICIES SET MED_POL_TITLE= 'TEST_XRAY PAYMENT REDUCTOIN POLICY' WHERE MED_POL_KEY="+sVal1+" AND RELEASE_LOG_KEY="+sVal2;
	           break;
	           
	       case "PREVIOUS DP SORTORDER CHANGE":
	           sba = "select SORT_ORDER from ELL.VDECISION_POINTS where DP_KEY =  "+sVal1+" and RELEASE_LOG_KEY = "+sVal2;           
	           break;
	           
	       case "UPDATE DP SORTORDER CHANGE":
	           sba = "UPDATE ELL.VDECISION_POINTS SET SORT_ORDER='"+sVal3+"' WHERE DP_KEY="+sVal1+" AND RELEASE_LOG_KEY="+sVal2 ;
	           break;
	           
	       case "PREVIOUS PRIMARY REFERENCE CHANGE":          
	           sba = " select PRIM_REF_SOURCE_DESC from ELL.VLIBRARY_VERSIONS where mid_rule_key = "+sVal1+" and release_log_key = "+sVal2;          
	           break;
	           
	       case "UPDATE PRIMARY REFRENCE CHANGE":
	           sba = "UPDATE ELL.VLIBRARY_VERSIONS SET PRIM_REF_SOURCE_DESC = 'TEST_CMSOUTPATIEN PROSPECTIVE PAYMENT SYSTEM' WHERE MID_RULE_KEY= "+sVal1+" AND RELEASE_LOG_KEY="+sVal2;;
	           break;
	           
	       case "PREVIOUS DP RECAT CHANGE":          
	           sba = "select TOPIC_KEY from ELL.VDECISION_POINTS where DP_KEY = "+sVal1+" and release_log_key = "+sVal2;          
	           break;
	           
	       case "UPDATE DP RECAT CHANGE":
	           sba = "UPDATE ELL.VDECISION_POINTS SET TOPIC_KEY ='"+sVal3+"' where DP_KEY = "+sVal1+" AND RELEASE_LOG_KEY="+sVal2;
	           break;
	           
	       case "PREVIOUS TOPIC RECAT CHANGE":          
	           sba = "select MED_POL_KEY from ELL.VTOPICS where TOPIC_KEY = "+sVal1+" and release_log_key = "+sVal2;          
	           break;
	           
	       case "UPDATE TOPIC RECAT CHANGE":
	           sba = "UPDATE ELL.VTOPICS SET MED_POL_KEY ='"+sVal3+"'  where  TOPIC_KEY = "+sVal1+" AND RELEASE_LOG_KEY="+sVal2;
	           break;
	       }
	       
	       return sba;
	       }

}


