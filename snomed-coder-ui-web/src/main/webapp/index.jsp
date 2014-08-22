<%--
  #%L
  NICTA t3as SNOMED CT GWT UI
  %%
  Copyright (C) 2014 NICTA
  %%
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  
  Additional permission under GNU GPL version 3 section 7
  
  If you modify this Program, or any covered work, by linking or combining
  it with H2, GWT, or JavaBeans Activation Framework (JAF) (or a
  modified version of those libraries), containing parts covered by the
  terms of the H2 License, the GWT Terms, or the Common Development and
  Distribution License (CDDL) version 1.0 ,the licensors of this Program
  grant you additional permission to convey the resulting work.
  #L%
  --%>
<!doctype html>

<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>SNOMED CT Text Analyser ${project.version}</title>
    <script type="text/javascript">
        document.cookie = "webserviceHost=<%= System.getProperty("webserviceHost", "") %>"
    </script>
    <script type="text/javascript" language="javascript" src="SnomedCoderUi/SnomedCoderUi.nocache.js"></script>
</head>

<body style="margin-left: 5%; margin-right: 5%;">

<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position:absolute;width:0;height:0;border:0"></iframe>
<noscript>
    <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
    </div>
</noscript>

<h1>
    t3as SNOMED CT Text Analyser
    <a href="http://nicta.com.au/">
        <img style="float:right; vertical-align:middle; margin: 0 10px 50px;" src="nicta_logo.jpg" alt="NICTA logo"
             width="87" height="104"/>
    </a>
    <a href="http://t3as.org/">
        <img style="float:right; vertical-align:middle; margin: 0 10px 50px;" src="The_Lab_Logo.jpg" alt="The Lab logo"
             width="110" height="110"/>
    </a>
</h1>
(v${project.version})
<p>
    This is a simple front-end for a public demo web service that will analyse English clinical text, and report any
    SNOMED CT concepts that can be detected. To do this it makes use of
    <a href="http://metamap.nlm.nih.gov/">NLM MetaMap</a> and
    <a href="http://www.nlm.nih.gov/research/umls/">NLM UMLS</a>. For more information please see the
    <a href="http://t3as.org/">t3as project website</a> and the <a href="http://t3as.wordpress.com/">t3as blog</a>.
</p>

<p>
    Full instructions on how to use the web service will be published in due course, but simple instructions can be
    had from the web service itself:
    <br/>
    <a href="/snomed-coder-web/rest/v1.0/snomedctCodes">/snomed-coder-web/rest/v1.0/snomedctCodes</a>
</p>

<p>
    <em><b>WARNING:</b></em>
    <b>DO NOT UPLOAD TEXT CONTAINING PRIVATE PERSONALLY IDENTIFIABLE INFORMATION ABOUT ANY PERSON.</b>
    This information cannot be kept private in this website, and will potentially be available to third parties as
    all text entered on this page is sent unencrypted in clear text to the service.
    This demonstration service runs on a publicly accessible server that is not geographically constrained.
</p>

<table>
    <tr>
        <td style="font-weight:bold;">Please enter the text to be analysed for SNOMED CT codes:</td>
    </tr>
    <tr>
        <td id="mainTextArea"></td>
    </tr>
    <tr>
        <td>
            <div id="analyseButton" style="display:inline-block;"></div>
            <div id="configureButton" style="display:inline-block;"></div>
            <div id="status" style="display:inline-block; float:right;"></div>
        </td>
    </tr>
</table>

<h3>SNOMED CT concepts found</h3>

<div id="snomedCodes"></div>

</body>
</html>
