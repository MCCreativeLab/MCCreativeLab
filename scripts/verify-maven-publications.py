#!/usr/bin/env python3
from pathlib import Path
import urllib.request,xml.etree.ElementTree as E,json,argparse
parser=argparse.ArgumentParser(description='Verify published snapshot checksums against locally built Gradle module metadata.')
parser.add_argument('--workspace',type=Path,default=Path(__file__).resolve().parents[2])
parser.add_argument('--output',type=Path)
args=parser.parse_args()
base=args.workspace.resolve()
modules=[('MCCreativeLab-Fork/mccreativelab-api','maven'),('MCCreativeLab-Fork/mccreativelab-server','devBundle'),('mcc-wrapper/api','maven'),('mcc-wrapper/vanilla','maven'),('mcc-wrapper/paper','maven'),('mcc-wrapper/paper/mod','maven'),('mcc-util','maven'),('mcc-pack-generator','maven'),('mcc-plugin-extension','maven')]
results=[]
for path,pub in modules:
 publication=base/path/'build/publications'/pub
 pom=E.parse(publication/'pom-default.xml').getroot(); ns={'m':'http://maven.apache.org/POM/4.0.0'}
 group,artifact,version=[pom.findtext('m:'+x,namespaces=ns) for x in ['groupId','artifactId','version']]
 rooturl='https://repo.verdox.de/snapshots/'+group.replace('.','/')+'/'+artifact+'/'+version+'/'
 metadata=E.fromstring(urllib.request.urlopen(rooturl+'maven-metadata.xml',timeout=30).read())
 extension='zip' if pub=='devBundle' else 'jar'
 snapshot=next(x.findtext('value') for x in metadata.findall('versioning/snapshotVersions/snapshotVersion') if x.findtext('extension')==extension and not x.findtext('classifier'))
 url=rooturl+artifact+'-'+snapshot+'.'+extension
 sha=urllib.request.urlopen(url+'.sha256',timeout=30).read().decode().strip().split()[0]
 module=json.loads((publication/'module.json').read_text())
 expected={f['sha256'] for v in module['variants'] for f in v.get('files',[]) if f['name']==artifact+'-'+version+'.'+extension}
 if sha not in expected:raise RuntimeError('Remote checksum differs from built artifact: '+artifact)
 result={'project':path,'coordinates':':'.join([group,artifact,version]),'snapshot':snapshot,'artifact_url':url,'sha256':sha,'checksum_matches_build':True}
 results.append(result);print(json.dumps(result),flush=True)
if args.output:
 args.output.write_text(json.dumps(results,indent=2)+'\n')
