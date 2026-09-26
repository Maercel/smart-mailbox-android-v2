const { initializeApp, cert } = require('firebase-admin/app');
const { getFirestore } = require('firebase-admin/firestore');
const crypto = require('crypto');

initializeApp({ credential: cert(require('./service-account.json')) });
const db = getFirestore()

// Case-sensitive, without look-alikes: no 0/O/o, 1/I/l
const ALPHABET = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789';
const CODE_LENGTH = 8;
const COUNT = 5;
const MODEL_IDS = ['small_v1', 'medium_v1', 'medium_v2', 'large_v1'];   // real ids from MailboxDeviceModel

const randomCode = () =>
  Array.from({ length: CODE_LENGTH }, () => ALPHABET[crypto.randomInt(ALPHABET.length)]).join('');

async function main() {
  const batch = db.batch();
  const codes = [];

  for (let i = 0; i < COUNT; i++) {
    const code = randomCode();
    codes.push(code);
    batch.create(db.collection('devices').doc(code), {   // create = fails if the code already exists
      deviceModelId: MODEL_IDS[crypto.randomInt(MODEL_IDS.length)],
      claimedBy: null,
      mailboxId: null,
    });
  }

  await batch.commit();
  console.log('Created devices:\n' + codes.join('\n'));
}

main().catch(console.error);