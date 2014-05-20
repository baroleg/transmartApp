<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>subsetPanel.html</title>


    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'dataAssociation.css')}">

</head>

<body>
<form>
    <table class="subsettable" style="margin: 10px;width:300px; border: 0px none; border-collapse: collapse;">
        <tr>
            <td align="center">
                <span class='AnalysisHeader'>VCF</span>
                <br/>
                <br/>
                Select a VCF node from the Data Set Explorer Tree and drag it into the box.
            </td>
        </tr>
        <tr>
            <td align="right">
                <input style="font: 9pt tahoma;" type="button" onclick="clearGroupVCF('divIndependentVariableVCF')"
                       value="X">
                <br/>

                <div id='divIndependentVariableVCF' class="queryGroupInclude"></div>
            </td>
        </tr>

        <tr>
            <td>
            </td>
        </tr>

        <tr>
            <td colspan="4" align="right">
                <input type="button" value="Run" onClick="submitVCF(this.form);">
            </td>
        </tr>
    </table>
</form>

<script type="text/javascript">
    setupDragAndDropForVCF('divIndependentVariableVCF');
</script>

</body>
</html>

