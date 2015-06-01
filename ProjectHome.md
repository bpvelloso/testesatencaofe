An frontend application to automate attention tests with NeuroSky Mindwave EEG devices

Author: Bruno Panerai Velloso bpvelloso@gmail.com

This project make use of ConectorNeuroSky for comunication with NeuroSky Thinkgear Connector.

More information about ConectorNeuroSky in:

https://code.google.com/p/conector-neurosky/

---

All the sources can be download at project home:
https://code.google.com/p/testesatencaofe/

The package with the last binary version of this application can be download at:
http://goo.gl/7gxCFS


To run TestesAtencaoFE:
Windows:
> - With Java SE 1.6 or newer installed:
> > Double click in TestesAtencaoFE.jar file;


> - in command line go to the application folder and type:
> > javaw -jar TestesAtencaoFE.jar

Linux:

> - in command line go to the application folder and type:
> > java -jar TestesAtencaoFE.jar


Configuration:

inside the archive configuracaoLocal exists an xml file with system´s
configurations, edit this file to include any new hypermedias adding a
entry as follow:
```
    <hipermidias>
        <titulo>Hipermedia Name</titulo>
        <URI>http://hipermidia.url/hipermidia</URI>
    </hipermidias>
```
The configuracaoLocal.xml.gz must be compressed with GZip algorithm.



Note:
This project are part of Doctoral thesis of Bruno Panerai Velloso in Knowledge
Engineering And Management at EGC/UFSC http://www.egc.ufsc.br


Copyright (c) 2014, Bruno Panerai Velloso
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.